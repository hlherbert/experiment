package com.hl.experiment.exam.doc;

import com.alibaba.fastjson.JSON;
import com.sun.javadoc.AnnotationDesc;
import com.sun.javadoc.AnnotationTypeDoc;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ParamTag;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.RootDoc;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class JavaDocReader {
    private static RootDoc root;

    private static Map<String, ApiClassDoc> classDocMap;

    // 一个简单Doclet,收到 RootDoc对象保存起来供后续使用
    // 参见参考资料6
    public static class Doclet {
        public Doclet() {
        }

        public static boolean start(RootDoc root) {
            JavaDocReader.root = root;
            return true;
        }
    }

    public JavaDocReader() {

    }


    // 显示DocRoot中的基本信息
    public static void show() {
        ClassDoc[] classes = root.classes();
        for (int i = 0; i < classes.length; ++i) {
            System.out.println(classes[i]);
            System.out.println(classes[i].commentText());
            for (MethodDoc method : classes[i].methods()) {
                System.out.printf("\t%s\n", method.commentText());
            }
        }
    }


    public static RootDoc getRoot() {
        return root;
    }
    public static Map<String, ApiClassDoc> getClassDocMap() {
        return classDocMap;
    }

    public static RootDoc readDoc(String sourcePath, String pkg) {
        // 调用com.sun.tools.javadoc.Main执行javadoc,参见 参考资料3
        // javadoc的调用参数，参见 参考资料1
        // -doclet 指定自己的docLet类名
        // -classpath 参数指定 源码文件及依赖库的class位置，不提供也可以执行，但无法获取到完整的注释信息(比如annotation)
        // -encoding 指定源码文件的编码格式


        //        String classPath = basePath+"/out/production/classes";

//        com.sun.tools.javadoc.Main.execute(new String[] {"-doclet",
//                Doclet.class.getName(),
//// 因为自定义的Doclet类并不在外部jar中，就在当前类中，所以这里不需要指定-docletpath 参数，
////              "-docletpath",
////              Doclet.class.getResource("/").getPath(),
//                "-encoding","utf-8",
////                "-classpath",
////                classPath,
//// 获取单个代码文件FaceLogDefinition.java的javadoc
//                "-sourcepath", sourcePath,
//                "-subpackages", pkg});

        com.sun.tools.javadoc.Main.execute(new String[]{"-doclet",
                Doclet.class.getName(),
                "-encoding", "utf-8",
                "-sourcepath", sourcePath,
                "-subpackages", pkg});

        classDocMap = buildClassMap(root);
        return root;
    }

    private static Map<String, ApiClassDoc> buildClassMap(RootDoc root) {
        Map<String, ApiClassDoc> theClassDocMap = new ConcurrentHashMap<>();
        ClassDoc[] classes = root.classes();
        for (ClassDoc classDoc:classes) {
            ApiClassDoc apiClassDoc = new ApiClassDoc(classDoc.qualifiedName(),classDoc.commentText());
            apiClassDoc.setMethodDocMap(buildMethodDocMap(classDoc));
            theClassDocMap.put(apiClassDoc.getName(), apiClassDoc);
        }
        return theClassDocMap;
    }

    private static Map<String, ApiMethodDoc> buildMethodDocMap(ClassDoc classDoc) {
        Map<String, ApiMethodDoc> methodMap = new ConcurrentHashMap<>();
        for (MethodDoc method : classDoc.methods()) {
            // method is identified by name+signature: addStudent(java.lang.String,java.lang.String)
            ApiMethodDoc apiMethodDoc = new ApiMethodDoc(getMethodId(method), method.commentText());

            ParamTag[] paramTags = method.paramTags();
            Parameter[] parameters = method.parameters();
            apiMethodDoc.setParamMap(buildParamMap(paramTags, parameters));
            methodMap.put(apiMethodDoc.getName(), apiMethodDoc);

            // for test
//            AnnotationDesc anno = method.annotations()[0];
//            anno.elementValues();
//            anno.annotationType()
//            System.out.println(anno);
//            Parameter[] params = method.parameters();
        }
        return methodMap;
    }

    private static Map<String, ApiParamDoc> buildParamMap(ParamTag[] paramTags, Parameter[] parameters) {
        ConcurrentHashMap<String, ApiParamDoc> paramMap = new ConcurrentHashMap<>();
        Map<String, Parameter> parameterMap = Arrays.asList(parameters).stream().collect(Collectors.toMap(x -> x.name(), x -> x));
        Map<String, ParamTag> parameterTagMap = Arrays.asList(paramTags).stream().collect(Collectors.toMap(x -> x.parameterName(), x -> x));

        for (Parameter parameter : parameters) {
            ParamTag tag = parameterTagMap.get(parameter.name());
            String typeName = parameter.type().qualifiedTypeName();
            ApiParamDoc paramDoc = new ApiParamDoc(typeName, parameter.name(), tag != null ? tag.parameterComment() : null);
            AnnotationDesc[] annos = parameter.annotations();
            for (AnnotationDesc anno : annos) {
                AnnotationDesc.ElementValuePair[] elements = anno.elementValues();
                AnnotationTypeDoc annoType = anno.annotationType();
                String annoTypeName = annoType.typeName();
                if (annoTypeName != null && annoTypeName.endsWith("RequestParam")) {
                    String value = null;
                    for (AnnotationDesc.ElementValuePair ele : elements) {
                        if (ele.element().name().equals("value")) {
                            value = ele.value().toString();
                        }
                    }
                    if (value != null) {
                        paramDoc.setRequestParamName(value);
                    }
                } else if (annoTypeName != null && annoTypeName.endsWith("RequestBody")) {
                    paramDoc.setBody(true);
                }
            }

            Object exampleObj = ExampleUtil.getExampleByTypeName(typeName);
            String example = JSON.toJSONString(exampleObj, true);
            paramDoc.setExample(example);
            paramMap.put(paramDoc.getName(), paramDoc);
        }
        return paramMap;
    }

    private static String getMethodId(MethodDoc method) {
        return method.name()+method.signature();
    }
}
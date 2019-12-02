package com.hl.experiment.exam.doc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * api docs生成器
 */
public class ApiDoc {

    public static List<Class<?>> getControllers(String pkgName) {
        List<Class<?>> controllers = null;
        if (controllers == null) {
            controllers = new ArrayList<>();
            List<Class<?>> clsList = ClassUtil.getClasses(pkgName);
            if (clsList != null && clsList.size() > 0) {
                for (Class<?> cls : clsList) {
                    // only add RestController & Controller
                    if (cls.getAnnotation(RestController.class) != null
                            || cls.getAnnotation(Controller.class) != null) {
                        controllers.add(cls);
                    }
                }
            }
        }
        return controllers;
    }

    public static List<ApiClass> collectApis(List<Class<?>> controllers, Map<String, ApiClassDoc> classDocMap) {
        List<ApiClass> apiClasses = new ArrayList<>();

        for (Class<?> controller : controllers) {
            ApiClassDoc classDoc = classDocMap.get(controller.getName());
            ApiClass apiClass = collectApis(controller, classDoc);
            apiClasses.add(apiClass);
        }
        return apiClasses;
    }

    private static ApiClass collectApis(Class<?> controller, ApiClassDoc classDoc) {
        assert (classDoc != null && controller!=null);
        ApiClass apiClass = new ApiClass(controller.getName());
        apiClass.setNote(classDoc.getCommentText());

        Map<String, ApiMethodDoc> methodDocMap = classDoc.getMethodDocMap();
        List<ApiEntry> apis = new ArrayList<>();
        Method[] methods = controller.getMethods();
        for (Method method : methods) {
            // only collect public methods
            if (method.getModifiers() != Modifier.PUBLIC) {
                continue;
            }
            RequestMapping anno = method.getAnnotation(RequestMapping.class);
            PostMapping postanno = method.getAnnotation(PostMapping.class);
            GetMapping getanno = method.getAnnotation(GetMapping.class);
            PutMapping putanno = method.getAnnotation(PutMapping.class);
            DeleteMapping delanno = method.getAnnotation(DeleteMapping.class);

            if (anno == null && getanno == null && postanno == null
                    && putanno == null && delanno == null) {
                continue;
            }

            ApiEntry apiEntry = new ApiEntry();
            RequestAnno rAnno = null;
            if (anno != null) {
                rAnno = RequestAnno.from(anno);
            } else if (postanno != null) {
                rAnno = RequestAnno.from(postanno);
            } else if (getanno != null) {
                rAnno = RequestAnno.from(getanno);
            } else if (putanno != null) {
                rAnno = RequestAnno.from(putanno);
            } else if (delanno != null) {
                rAnno = RequestAnno.from(delanno);
            } else {
                continue;
            }

            String methodId = getMethodId(method);
            ApiMethodDoc methodDoc = methodDocMap.get(methodId);
            apiEntry.setMethod(methodId);
            extractFromAnno(rAnno, apiEntry);
            extractFromParams(method, apiEntry);
            extractFromMethodDoc(methodDoc, apiEntry);
            apis.add(apiEntry);

//            if (value.equals("/about")) {
//                //判断是不是/about，是的话，就调用about(args)方法
//                method.invoke(cls.newInstance(), "args"); //第二个参数是方法里的参数
//            }
        }
        apiClass.setApis(apis);
        return apiClass;
    }

    private static String getMethodId(Method method) {
        return method.getName()+"("+String.join(", ",
                Arrays.stream(method.getParameterTypes()).map(x->x.getName()).collect(Collectors.toList()))
                + ")";
    }

    private static void outputApis(Collection<ApiClass> apiClasses) {
        for (ApiClass apiClass : apiClasses) {
            System.out.println(apiClass);
            System.out.println("=============");
        }
    }

    private static void extractFromAnno(RequestAnno anno, ApiEntry apiEntry) {
        apiEntry.setPath(anno.getPath());
        apiEntry.setHttpMethod(anno.getMethod());
    }

    private static void extractFromParams(Method method, ApiEntry apiEntry) {
// note should from java doc of this method
        apiEntry.setMethod(method.getName());

        String body = null;
        List<String> paramList = new ArrayList<>();
        Parameter[] params = method.getParameters();
        for (Parameter param : params) {
            RequestParam paramAnno = param.getAnnotation(RequestParam.class);
            if (paramAnno != null) {
                String paramName = paramAnno.name();
                if (paramName == null || paramName.isEmpty()) {
                    paramName = param.getName();
                }
                paramList.add(paramName);
                continue;
            }
            RequestBody bodyAnno = param.getAnnotation(RequestBody.class);
            if (bodyAnno != null) {
                body = MessageFormat.format("{0}:{1}", param.getType().getName(), param.getName());
                continue;
            }
        }
        String[] paramArr = new String[paramList.size()];
        paramList.toArray(paramArr);
        apiEntry.setQueryParams(paramArr);
        apiEntry.setBody(body);
    }

    private static void extractFromMethodDoc(ApiMethodDoc methodDoc, ApiEntry apiEntry) {
        if (methodDoc==null) {
            return;
        }
        apiEntry.setNote(methodDoc.getCommentText());
    }

    public static void main(String[] args) {
        String pkg = "com.hl.experiment.exam.controller";
        String basePath = "D:/gitproject/experiment/exam-server";
        String sourcePath = basePath + "/src/main/java";

        JavaDocReader.readDoc(sourcePath, pkg);
        List<Class<?>> controllers = getControllers(pkg);
        List<ApiClass> apiClasses = collectApis(controllers, JavaDocReader.getClassDocMap());
        outputApis(apiClasses);
    }
}

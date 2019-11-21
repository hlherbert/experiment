package com.hl.algo.dividen;


import java.awt.geom.Path2D;

/**
 * 椭圆形状
 */
public abstract class Eclipse2D extends Path2D.Float {

    /**
     * 用Bezier曲线模拟圆弧。
     *
     * @param cx          弧心
     * @param cy          弧心
     * @param rx          横轴半径
     * @param ry          纵轴半径
     * @param start_angle 起始角度
     * @param sweep_angle 扫描角度
     * @return 贝塞尔曲线的4个控制点[x1, y1, x2, y2, x3, y3, x4, y4]
     */
    protected static double[] arcToBezier(double cx, double cy, double rx, double ry,
                                          double start_angle, double sweep_angle) {
        double x0 = Math.cos(sweep_angle / 2.0);
        double y0 = Math.sin(sweep_angle / 2.0);
        double tx = (1.0 - x0) * 4.0 / 3.0;
        double ty = y0 - tx * x0 / y0;
        double[] px = new double[4];
        double[] py = new double[4];
        px[0] = x0;
        py[0] = -y0;
        px[1] = x0 + tx;
        py[1] = -ty;
        px[2] = x0 + tx;
        py[2] = ty;
        px[3] = x0;
        py[3] = y0;

        double sn = Math.sin(start_angle + sweep_angle / 2.0);
        double cs = Math.cos(start_angle + sweep_angle / 2.0);

        double[] curve = new double[8];
        for (int i = 0; i < 4; i++) {
            curve[i * 2] = cx + rx * (px[i] * cs - py[i] * sn);
            curve[i * 2 + 1] = cy + ry * (px[i] * sn + py[i] * cs);
        }
        return curve;
    }


    public static class Float extends Eclipse2D {
        public float x;
        public float y;
        public float r;

        public Float(float cx, float cy, float rx, float ry) {
            double startAngle = 0;
            double sweepAngle = Math.PI * 0.5;

            double[] curve = arcToBezier(cx, cy, rx, ry, startAngle, sweepAngle);
            this.moveTo(curve[0], curve[1]);
            this.curveTo(curve[2], curve[3], curve[4], curve[5], curve[6], curve[7]);

            curve = arcToBezier(cx, cy, rx, ry, startAngle + sweepAngle, sweepAngle);
            this.curveTo(curve[2], curve[3], curve[4], curve[5], curve[6], curve[7]);

            curve = arcToBezier(cx, cy, rx, ry, startAngle + 2 * sweepAngle, sweepAngle);
            this.curveTo(curve[2], curve[3], curve[4], curve[5], curve[6], curve[7]);


            curve = arcToBezier(cx, cy, rx, ry, startAngle + 3 * sweepAngle, sweepAngle);
            this.curveTo(curve[2], curve[3], curve[4], curve[5], curve[6], curve[7]);
        }
    }
}

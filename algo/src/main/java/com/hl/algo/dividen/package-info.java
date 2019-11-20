/**
 * 用分治法嵌套绘图。发现图形的模式。
 * <p>
 * 初始条件：深度depth = MAX_DEPTH
 * 1.将绘图步骤F(R)应用到矩形R
 * 2.将R以中线分割为4个大小相同的子矩形
 * 3.depth := depth-1
 * 4.对4个子矩形作为R，重复以上步骤, 直到深度depth=0。
 */
package com.hl.algo.dividen;
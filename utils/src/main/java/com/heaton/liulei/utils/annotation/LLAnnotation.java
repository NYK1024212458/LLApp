package com.heaton.liulei.utils.annotation;

import android.app.Activity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 自定义注解  用于替代findViewById
 * Created by LiuLei on 2017/6/9.
 */

public class LLAnnotation {

    public static void viewInit(Activity activity){
        Class<?> cls = activity.getClass();
        Field[] fields = cls.getDeclaredFields();
        if(fields != null && fields.length > 0){
            for (Field field : fields){
                //获取字段的注解，如果没有ViewInit注解，则返回null
                ViewInit viewInit = field.getAnnotation(ViewInit.class);
                if(viewInit != null){
                    //获取字段注解的参数，这就是我们传进去控件ID
                    int viewId = viewInit.value();
                    if(viewId != -1){
                        try {
                            // 获取类中的findViewById方法，参数为int
                            Method method = cls.getMethod("findViewById",int.class);
                            //执行该方法，返回一个Object类型的View实例
                            Object resView = method.invoke(activity,viewId);
                            field.setAccessible(true);
                            //把字段的值设置为该View的实例
                            field.set(activity,resView);
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        }catch (IllegalAccessException e){
                            e.printStackTrace();
                        }catch (InvocationTargetException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}

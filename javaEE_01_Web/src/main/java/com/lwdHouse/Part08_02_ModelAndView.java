package com.lwdHouse;

import java.util.Map;

public class Part08_02_ModelAndView {
    // 数据
    Map<String, Object> model;
    // 模板的路径
    String view;

    public Part08_02_ModelAndView(Map<String, Object> model, String view) {
        this.model = model;
        this.view = view;
    }

    public Part08_02_ModelAndView(Map<String, Object> model) {
        this(model, null);
    }

    public Part08_02_ModelAndView(String view) {
        this(null, view);
    }
}
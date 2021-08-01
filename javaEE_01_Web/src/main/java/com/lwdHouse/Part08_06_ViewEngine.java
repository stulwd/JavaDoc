package com.lwdHouse;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ServletLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.Writer;

public class Part08_06_ViewEngine {
    private final PebbleEngine engine;


    public Part08_06_ViewEngine(ServletContext servletContext) {
        ServletLoader loader = new ServletLoader(servletContext);
        loader.setCharset("UTF-8");
        /* 设置模板的前后缀 */
        loader.setPrefix("/WEB-INF/templates");
        loader.setSuffix("");
        this.engine = new PebbleEngine.Builder().autoEscaping(true).cacheActive(false)
                .loader(loader).build();
    }

    public void render(Part08_02_ModelAndView mv, Writer writer) throws IOException {
        PebbleTemplate template = this.engine.getTemplate(mv.view);
        template.evaluate(writer, mv.model);
    }
}

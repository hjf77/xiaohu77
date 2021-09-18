package com.fhs.pagex.controller;

import com.fhs.common.utils.CheckUtils;
import com.fhs.core.result.HttpResult;
import com.fhs.logger.Logger;
import com.fhs.pagex.service.PageXAutoJavaService;
import com.fhs.pagex.service.PageXAutoSqlService;
import com.fhs.pagex.service.PagexDataService;
import com.fhs.pagex.trans.PageXTransServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * pagex 自动生成java代码控制器
 *
 * @author user
 * @date 2020-05-19 13:56:33
 */
@RestController()
@RequestMapping("/pagex/")
public class PageXAutoCodeController implements ApplicationRunner {

    private static final Logger LOGGER = Logger.getLogger(PageXAutoCodeController.class);

    @Autowired
    private PageXAutoJavaService pageXAutoJavaService;

    @Autowired
    private PageXAutoSqlService pageXAutoSqlService;

    @Autowired
    private PageXTransServiceImpl pageXTransService;

    @RequestMapping("/autoJavaAndSql")
    @ResponseBody
    public HttpResult<Boolean> autoCode(String namespace) throws Exception {
        if (CheckUtils.isNotEmpty(namespace)) {
            try {
                String js = PagexDataService.SIGNEL.getJsContent(namespace);
                pageXAutoJavaService.autoJava(js);
                pageXAutoSqlService.autoSql(js);
            } catch (Exception e) {
                LOGGER.error("生成代码错误:", e);
            }
        } else {
            Set<String> namespaceSet = PagexDataService.SIGNEL.getAllJsNamespace();
            final CountDownLatch countDownLatch = new CountDownLatch(namespaceSet.size());
            for (String tempNamepsace : namespaceSet) {
                final String js = PagexDataService.SIGNEL.getJsContent(tempNamepsace);
                new Thread(() -> {
                    try {
                        pageXAutoJavaService.autoJava(js);
                        pageXAutoSqlService.autoSql(js);
                        countDownLatch.countDown();
                    } catch (Exception e) {
                        LOGGER.error("生成代码错误:", e);
                    }
                }).start();
            }
            countDownLatch.await();

        }
        return HttpResult.success(true);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(() -> {
            try {
                autoCode(null);
                pageXTransService.refreshPageXCache(new HashMap<>());
            } catch (Exception e) {
                LOGGER.error("生成代码或者刷新pagex缓存错误:", e);
            }
        }).start();
    }
}

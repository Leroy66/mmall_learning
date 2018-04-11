package com.mmall.util.webmagic;

import com.mmall.util.download.DownloadImage;
import com.smartwork.msip.cores.helper.DateTimeHelper;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.File;
import java.util.List;

public class WallZOLPaperPageProcessor implements PageProcessor {
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    private String category;
    private String date;
    @Override
    public void process(Page page) {
        System.out.println(page.getUrl());
        // 部分二：定义如何抽取页面信息，并保存下来
        List<String> albumImageUrls = page.getHtml().xpath("//div[@class='main']/ul/li/a[@class='pic']/@href").all();
        List<String> ratioImageUrls = page.getHtml().xpath("//div[@class='wrapper mt15']/dl/dd/a/@href").all();
        if(!albumImageUrls.isEmpty()){//一级分页页面
            page.addTargetRequests(albumImageUrls);
            List<String> pageImageUrls = page.getHtml().xpath("//div[@class='page']/a/@href").all();
            if(!pageImageUrls.isEmpty()){
                System.out.println("~~~~~:"+pageImageUrls);
                page.addTargetRequests(pageImageUrls);
            }
            //分页处理
            page.setSkip(true);
        }else if(!ratioImageUrls.isEmpty()){
            page.addTargetRequests(ratioImageUrls);
            //List<String> pageImageUrls = page.getHtml().xpath("//a[@class='next']/@href").all();
            List<String> pageImageUrls = page.getHtml().xpath("//div[@class='photo-list-box']/ul/li/a/@href").all();
            if(!pageImageUrls.isEmpty()){
                page.addTargetRequests(pageImageUrls);
            }
            page.setSkip(true);
            /*page.putField("imgalt",page.getHtml().xpath("//div[@class='content_right']/div/p[@class='title']/text()").toString());
            String imgurl = page.getHtml().xpath("//div[@id='focus']/a/@cur").toString();
            page.putField("imgurl",imgurl);
            List<String> tags = page.getHtml().xpath("//div[@id='normal']/div[@class='crumbs']/a/text()").all();
            download_images("/VCData/data/download/"+date+"/app111/"+device+"/"+category+"/",
                    imgurl,tags,
                    wallPaperLinkMD5Service);*/
        }else{
            String imgUrl = page.getHtml().xpath("//body/img/@src").toString();
            page.putField("pageUrl",page.getUrl().toString());
            page.putField("imgUrl",imgUrl);
            try {
                // 根据图片URL 下载图片方法
                /**
                 * String 图片URL地址
                 * String 图片名称
                 * String 保存路径
                 */
                String pageUrl = page.getUrl().toString();
                int start = pageUrl.lastIndexOf("/");
                int end = pageUrl.lastIndexOf(".html");
                String fileName = pageUrl.substring(start+1,end);
                page.putField("fileName",fileName);

                //240x320_90434_86
                String[] array = fileName.split("_");
                DownloadImage.download(imgUrl,array[0].concat(".jpg"),"/data/images/".concat(category).concat(File.separator).concat(array[2]).concat(File.separator).concat(array[1]));
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static void main(String[] args) {
        //ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath*:/com/naola/di/business/dataimport/dataImportCtx.xml");
        //WallPaperLinkMD5Service wallPaperLinkMD5Service = (WallPaperLinkMD5Service)ctx.getBean("wallPaperLinkMD5Service");
        FilePipeline filePipeline = new FilePipeline();
        filePipeline.setPath("/data/file/zol/");
        String date = DateTimeHelper.formatDate(DateTimeHelper.FormatPattern5);

        String[] categories = {
                //"风景",
                //"美女",
                "动漫"
        };

        String[][] categories_url = {
                //new String[]{"http://sj.zol.com.cn/bizhi/fengjing/"},
                //new String[]{"http://sj.zol.com.cn/bizhi/meinv/"},
                new String[]{"http://sj.zol.com.cn/bizhi/dongman/"}
        };
        //String[] devices = {"iphone-5-wallpapers","iphone-wallpapers","ipad-wallpapers"};
        String[] devices = {"iphone"};
        int index = 0;
        for(String category:categories){
            WallZOLPaperPageProcessor categroyProc = new WallZOLPaperPageProcessor();
            //categroyProc.setWallPaperLinkMD5Service(wallPaperLinkMD5Service);
            //categroyProc.setDevice(devices[0]);
            categroyProc.setCategory(category);
            categroyProc.setDate(date);
            Spider.create(categroyProc)
                    .addUrl(categories_url[index])
                    //开启5个线程抓取
                    .thread(10)
                    .addPipeline(new ConsolePipeline())
                    //.setDownloader(new HttpClientDownloader())
                    //.addPipeline(filePipeline)
                    //启动爬虫
                    .run();
            index ++;
        }
    }
}

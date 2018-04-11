package com.mmall.util.webmagic;

import com.smartwork.msip.cores.helper.DateTimeHelper;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

public class LikewallpaperPageProcessor implements PageProcessor {
    //private WallPaperLinkMD5Service wallPaperLinkMD5Service;
    private String date;
    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(10*1000).setCharset("UTF-8")
            .setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.94 Safari/537.36");
    ;
    @Override
    // process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
    public void process(Page page) {
        // 部分二：定义如何抽取页面信息，并保存下来
        System.out.println(page.getUrl());
        List<String> realImageUrls = page.getHtml().xpath("//div[@id='divWalllist']/dl/dd[@class='cover']/a/@href").all();
        if(!realImageUrls.isEmpty()){//一级分页页面
            page.addTargetRequests(realImageUrls);
            System.out.println(realImageUrls);
            List<String> result = page.getHtml().links().regex(".*/Wallpapers/Date/\\w+").all();
            page.addTargetRequests(result);
            page.setSkip(true);
        }else{
            page.putField("imgAlt",page.getHtml().xpath("//div[@class='wallpaper']/div[@class='wallpaperEntry']/img/@alt").toString());
            String imgurl = page.getHtml().xpath("//div[@class='wallpaper']/div[@class='wallpaperEntry']/img/@src").toString();
            page.putField("imgUrl",imgurl);
            List<String> tags = page.getHtml().xpath("//dl[@class='tags clearfix']/dd/a/em/text()").all();
            page.putField("imgTags",tags);
            String views = page.getHtml().xpath("//div[@class='wallInfo']/dl[@class='views clearfix']/dd/span/text()").toString();
            page.putField("imgViews",views);
            String resolution = page.getHtml().xpath("//div[@class='wallInfo']/dl[@class='resolution clearfix']/dd/span/text()").toString();
            page.putField("imgesolution",resolution);
            /*for(String tag:tags){
    			System.out.println("~~~~~~~~~~:"+tags);
    		}*/
            /*download_images("/VCData/data/download/"+date+"/likewallpaper/"+device+"/"+category+"/",
                    "http://www.ilikewallpaper.net"+imgurl,tags,
                    wallPaperLinkMD5Service);*/
        }
        //page.putField("imgurl", page.getHtml().xpath("//div[@id='divWalllist']/dl/dt/a/@href").all());
        //page.putField("imgurl", page.getHtml().xpath("//div[@id='divWalllist']/dl/dt/a/img/@src").all());//img/@src"));
        /*page.putField("author", page.getUrl().regex("https://github\\.com/(\\w+)/.*").toString());
        page.putField("name", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
        if (page.getResultItems().get("name") == null) {
            //skip this page
            page.setSkip(true);
        }
        page.putField("avatar", page.getHtml().xpath("//img[@class='gravatar js-avatar']/@src").toString());
        page.putField("readme", page.getHtml().xpath("//div[@id='readme']/tidyText()"));*/

        // 部分三：从页面发现后续的url地址来抓取
        ///ipad-air/Nature%20-%20Landscape/Wallpapers/Date/2

        List<String> result = page.getHtml().links().regex(".*/Wallpapers/Date/\\w+").all();
        //System.out.println(result);
        page.addTargetRequests(result);//page.getHtml().links().regex("/Wallpapers/Date/\\w+").all());
        //page.addTargetRequests(page.getHtml().links().regex("(https://github\\.com/\\w+/\\w+)").all());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        //ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath*:/com/naola/di/business/dataimport/dataImportCtx.xml");
        //WallPaperLinkMD5Service wallPaperLinkMD5Service = (WallPaperLinkMD5Service)ctx.getBean("wallPaperLinkMD5Service");
        String date = DateTimeHelper.formatDate(DateTimeHelper.FormatPattern5);

        String[] categories = {"Anime - Cartoons","Love","Beauty - Stars","Nature - Landscape","Pets - Animals",
                "Auto - Vehicles","Art - Drawn","Abstract","Sport","Technology","Holiday - Event","Game",
                "movies - tv","Space","Military","Food - Drink","Macro"};

        String[] categories_url = {"Anime%20-%20Cartoons","Love","Beauty%20-%20Stars","Nature%20-%20Landscape","Pets%20-%20Animals",
                "Auto%20-%20Vehicles","Art%20-%20Drawn","Abstract","Sport","Technology","Holiday%20-%20Event","Game",
                "movies%20-%20tv","Space","Military","Food%20-%20Drink","Macro"};
        //String[] devices = {"iphone-5-wallpapers","iphone-wallpapers","ipad-wallpapers"};
        String[] devices = {"iphone-x","iphone-8"};
        for(String device:devices){
            int index = 0;
            for(String category:categories){
                System.out.println("http://www.ilikewallpaper.net/"+device+"/"+categories_url[index]+"/Wallpapers/Date/1");
                LikewallpaperPageProcessor categroyProc = new LikewallpaperPageProcessor();
                //categroyProc.setWallPaperLinkMD5Service(wallPaperLinkMD5Service);
                categroyProc.setDevice(device);
                categroyProc.setCategory(category);
                categroyProc.setDate(date);
                Spider.create(categroyProc)
                        //从"https://github.com/code4craft"开始抓
                        //.addUrl("http://www.ilikewallpaper.net/ipad-air/Nature%20-%20Landscape/Wallpapers/Date/1")
                        .addUrl("http://www.ilikewallpaper.net/"+device+"/"+categories_url[index]+"/Wallpapers/Date/1")
                        //.addPipeline(new JsonFilePipeline("/VCData/data/download/likewallpaper/"+device+"/"+category+"/"))
                        //开启5个线程抓取
                        .thread(5)
                        .addPipeline(new ConsolePipeline())

                        //启动爬虫
                        .run();
                index++;
            }
        }
    }

    public static String image_filename(String img_url){
        return img_url.substring(img_url.lastIndexOf("/") + 1);
    }

/*    public static void download_images(String download_path,String img_url,List<String> tags,
                                       WallPaperLinkMD5Service wallPaperLinkMD5Service){
        if(wallPaperLinkMD5Service.linkExists(img_url)) {
            System.out.println("Url link has been down before:"+img_url);
            return;
        }
        File image_rootdown = new File(download_path+"downimages/");
        if(!image_rootdown.isDirectory()){
            image_rootdown.mkdirs();
        }
        String filename = image_filename(img_url);
        InputStream in = null;
        OutputStream out = null;
        try{
            in = new BufferedInputStream(new URL(img_url).openStream());
            out = new FileOutputStream(image_rootdown.getAbsolutePath()+File.separator+filename);
            IOUtils.copy(in, out);
        }catch(Exception ex){
            ex.printStackTrace(System.out);
        }finally{
            if(in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
            if(out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                }
        }

        File tag_rootdown = new File(download_path+"downtags/");
        if(!tag_rootdown.isDirectory()){
            tag_rootdown.mkdirs();
        }
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter  bw = null;
        try{
            fos=new FileOutputStream(new File(tag_rootdown.getAbsolutePath()+File.separator+filename+".txt"));
            osw=new OutputStreamWriter(fos, "UTF-8");
            bw=new BufferedWriter(osw);
            for(String tag:tags){
                bw.write(tag+"\n");
            }
            bw.close();
            osw.close();
            fos.close();
        }catch(IOException ex){
            ex.printStackTrace(System.out);
        }finally{
            try {
                if(bw != null)
                    bw.close();
                if(osw != null)
                    osw.close();
                if(fos != null)
                    fos.close();

            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        }
    }*/

    private String device;
    private String category;


    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

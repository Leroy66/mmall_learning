package com.mmall.util.webmagic;

import com.mmall.util.download.DownloadMovie;
import com.smartwork.msip.cores.helper.DateTimeHelper;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

public class DoubanMoviePageProcessor implements PageProcessor {

	 // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    private String category;
    private String date;
    @Override
    public void process(Page page) {
        // 部分二：定义如何抽取页面信息，并保存下来
        //list的内容
        List<String> albumImageUrls = page.getHtml().xpath("//div[@class='mod_figures mod_figure_v']/ul/li/a/@href").all();
    	//详情页的内容
        if(!albumImageUrls.isEmpty()){//一级分页页面
            page.addTargetRequests(albumImageUrls);
            //下一页的内容
            List<String> pageImageUrls = page.getHtml().xpath("//div[@class='mod_pages']/span/a/@href").all();
            if(!pageImageUrls.isEmpty()){
            	List<String> urls=new ArrayList<String>();
            	for (String pageImageUrl : pageImageUrls) {
            		String url="https://v.qq.com/x/list/movie"+pageImageUrl;
            		urls.add(url);
				}
            	page.addTargetRequests(urls);
            }
            //分页处理
            page.setSkip(true);
        }else{
            String name = page.getHtml().xpath("//div[@class='mod_title']/h2/text()").toString();
            page.putField("pageUrl",page.getUrl().toString());
            page.putField("name",name);
            System.out.println("名字"+name);
            
            try{
                //下载视频

            	//DownloadMovie.downLoadFromUrl("","","");
            }catch(Exception e){
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
        FilePipeline filePipeline = new FilePipeline();
        filePipeline.setPath("/data/file/douban/");
        String date = DateTimeHelper.formatDate(DateTimeHelper.FormatPattern5);
        String[] categories = {
                "豆瓣好评"
        };
        String[][] categories_url = {
                new String[]{"https://v.qq.com/x/list/movie?sort=21&offset=0"}
        };
        int index = 0;
        for(String category:categories){
        	DoubanMoviePageProcessor categroyProc = new DoubanMoviePageProcessor();
            categroyProc.setCategory(category);
            categroyProc.setDate(date);
            Spider.create(categroyProc)
                    .addUrl(categories_url[index])
                    .thread(10)
                    .addPipeline(new ConsolePipeline())
                    .run();
            index ++;
        }
    }

}

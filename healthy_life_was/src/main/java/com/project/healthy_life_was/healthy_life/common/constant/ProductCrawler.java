package com.project.healthy_life_was.healthy_life.common.constant;

import com.project.healthy_life_was.healthy_life.dto.product.CrawledProductDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class ProductCrawler {

    public CrawledProductDto crawl(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(5000)
                    .get();

            String name = doc.select("h2.product_name").text();
            String priceText = doc.select(".price_real string").text()
                    .replace(",", "")
                    .replace("원", "");
            int price = Integer.parseInt(priceText);

            String imgUrl = doc.select(".product_detail_img img")
                    .attr("src");

            String origin = doc.select(".info_table tr:contains(원산지) td").text();
            String nutrition = doc.select(".info_table tr:contains(영양정보) td").text();

            return CrawledProductDto.builder()
                    .name(name)
                    .price(price)
                    .imageUrl(imgUrl)
                    .origin(origin)
                    .nutrition(nutrition)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("크롤링 실패: " + url, e);
        }
    }
}

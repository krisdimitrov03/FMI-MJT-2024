package bg.sofia.uni.fmi.mjt.cooking.http.response;

import com.google.gson.annotations.SerializedName;

public record ResponseDTO(@SerializedName("_links") LinksDTO links, HitDTO[] hits) {
}
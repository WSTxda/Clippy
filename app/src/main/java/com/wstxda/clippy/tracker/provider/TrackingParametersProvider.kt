package com.wstxda.clippy.tracker.provider

object TrackingParametersProvider {
    fun getTrackingFilterList(): Set<String> {
        return setOf(
            "a_id", "ab", "ab_id", "ad", "ad_id", "ad_name", "ad_position", "adgroup",
            "adgroup_id", "adgroup_name", "adset_id", "adset_name", "ad_type",
            "aff_click_id", "aff_id", "aff_network", "aff_source", "aid", "bid",
            "bid_id", "campaign_content", "campaign_id", "campaign_medium",
            "campaign_name", "campaign_source", "ch", "ch_id", "channel", "click_ref",
            "clickid", "cost", "cref", "conversion_id", "conversion_type", "coupon",
            "cid", "clid", "device", "device_id", "device_type", "event",
            "event_category", "event_label", "event_name", "external_id", "fb_event",
            "fbclid", "fb_user_id", "first_name", "gclid", "hsa_acc", "hsa_ad",
            "hsa_cam", "hsa_grp", "hsa_net", "hsa_src", "hsa_ver", "id", "igshid",
            "impression_id", "irclickid", "jsessionid", "keyword", "last_name",
            "loyalty_id", "matchtype", "mc_cid", "mc_eid", "m_id", "mkt_campaign",
            "mkt_content", "mkt_id", "mkt_source", "mkt_term", "msclkid", "network",
            "network_id", "offer_id", "offer_name", "order_id", "placement",
            "placement_id", "platform", "promo", "promo_code", "pub_id", "pub_source",
            "redirect", "ref", "referrer", "refid", "region", "revenue",
            "search_term", "session", "session_id", "sid", "s_cid", "s_kwcid",
            "sku", "social_id", "source", "source_id", "sub_id", "subid",
            "subid1", "subid2", "subid3", "tag", "tag_id", "ticket", "timestamp",
            "track", "tracking_id", "transaction_id", "transaction_type", "trk",
            "trk_id", "trk_source", "user_agent", "user_id", "user_type",
            "vid", "v_id", "visitor_id", "vurl", "waid", "wickedid", "zid", "_t", "_r"
        )
    }
}
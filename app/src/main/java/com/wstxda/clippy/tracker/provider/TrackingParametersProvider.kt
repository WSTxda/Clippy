package com.wstxda.clippy.tracker.provider

object TrackingParametersProvider {
    fun getTrackingFilterList(): Set<String> {
        return setOf(
            "ad", "ad_id", "ad_name", "adgroup", "adgroup_id", "adset_id",
            "aff_id", "campaign_content", "campaign_id", "campaign_medium",
            "campaign_name", "campaign_source", "clickid", "conversion_id",
            "cref", "device_id", "event", "fbclid", "gclid", "hsa_acc",
            "hsa_ad", "hsa_cam", "hsa_grp", "hsa_net", "hsa_src", "igshid",
            "jsessionid", "keyword", "mc_cid", "mc_eid", "mkt_campaign",
            "mkt_content", "msclkid", "promo", "promo_code", "ref", "referrer",
            "session_id", "sid", "source", "subid", "tag", "timestamp",
            "tracking_id", "trk_id", "user_id", "utm_source", "utm_medium",
            "utm_campaign", "utm_content", "utm_term"
        )
    }
}
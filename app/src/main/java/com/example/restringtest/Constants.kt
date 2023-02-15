package com.example.restringtest

/**
 * Created by allenhuang on 2018/4/12.
 */
object Constants {

    const val SETTING_PREFERENCE = "setting"

    object LANGUAGE {
        const val APP_LANGUAGE = "applanguage"

        //之後可以為了API參數做修改 將這裡的值當作API參數使用----------------------------
        const val TRADITIONAL_CHINESE = "zh-hk"
        const val SIMPLIFIED_CHINESE = "zh-cn"
        const val ENGLISH = "en-us"
        const val VIETNAMESE = "vi-vn"
        const val HINDI = "hi-in"
        const val JAPANESE = "ja-jp"
        const val KOREAN = "ko-kr"
        const val THAI = "th-th"
        const val PORTUGAL = "pt-br"
        const val SPANISH = "es-es"

        // 印尼的LanguageCode-CountryCode比較特別, Android的這邊是in-id, 但API的部分是用id-id, 所以這邊為了能讓API可以用修改為id-id
        // 這邊不會影響拿res的部分
        const val INDONESIAN = "id-id"

        //------------------------------------------------------------------------------
        const val LANGUAGE_CODE_CHINESE = "zh"
        const val LANGUAGE_CODE_ENGLISH = "en"
        const val LANGUAGE_CODE_VIETNAMESE = "vi"
        const val LANGUAGE_CODE_HINDI = "hi"
        const val LANGUAGE_CODE_JAPANESE = "ja"
        const val LANGUAGE_CODE_KOREAN = "ko"
        const val LANGUAGE_CODE_THAI = "th"
        const val LANGUAGE_CODE_PORTUGAL = "pt"
        const val LANGUAGE_CODE_SPANISH = "es"
        const val LANGUAGE_CODE_INDONESIAN = "in"
        const val COUNTRY_CODE_HK = "HK"
        const val COUNTRY_CODE_TW = "TW"
        const val COUNTRY_CODE_MO = "MO"
        const val COUNTRY_CODE_CN = "CN"
        const val COUNTRY_CODE_IN = "IN"
    }

}
package com.igniters.sm.sandbox.json_redis;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data

public class Instrument {

        
        @JsonProperty("exchangeSegment")
        private String exchangeSegment;

        @JsonProperty("exchangeInstrumentID")
        private String exchangeInstrumentID;
        @JsonProperty("instrumentType")
        private String instrumentType;
        @JsonProperty("name")
        private String name;

        
        @JsonProperty("description")
        private String description;
        @JsonProperty("series")
        private String series;
        @JsonProperty("nameWithSeries")
        private String nameWithSeries;
        @JsonProperty("instrumentID")
        private String instrumentID;
        @JsonProperty("priceBandHigh")
        private String priceBandHigh;
        @JsonProperty("priceBandLow")
        private String priceBandLow;
        @JsonProperty("freezeQty")
        private String freezeQty;
        @JsonProperty("tickSize")
        private String tickSize;
        @JsonProperty("lotSize")
        private Integer lotSize;
        @JsonProperty("multiplier")
        private Integer multiplier;
        @JsonProperty("underlyingInstrumentId")
        private String underlyingInstrumentId;
        @JsonProperty("underlyingIndexName")
        private String underlyingIndexName;

        @JsonProperty("contractExpiration")
        private String contractExpiration;
        @JsonProperty("strikePrice")
        private String strikePrice;
        @JsonProperty("optionType")
        private String optionType;
        @JsonProperty("displayName")
        private String displayName;

        @JsonProperty("priceNumerator")
        private Integer priceNumerator;

        @JsonProperty("priceDenominator")
        private Integer priceDenominator;
        
        @JsonProperty("detailedDescription")
        private String detailedDescription;

        
        @JsonProperty("quoteData")
        private QuotesData quoteData;
}

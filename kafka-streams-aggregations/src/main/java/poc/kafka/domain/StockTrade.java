package poc.kafka.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StockTrade {

	@JsonProperty("SYMBOL")
	private String symbol;
	@JsonProperty("SERIES")
	private String series;
	@JsonProperty("OPEN")
	private double open;
	@JsonProperty("HIGH")
	private double high;
	@JsonProperty("LOW")
	private double low;
	@JsonProperty("CLOSE")
	private double close;
	@JsonProperty("LAST")
	private double last;
	@JsonProperty("PREVCLOSE")
	private double prevClose;
	@JsonProperty("TOTTRDQTY")
	private long totTrdQty; // Used long for totTrdQty
	@JsonProperty("TOTTRDVAL")
	private double totTrdVal;
	@JsonProperty("TIMESTAMP")
	private String timestamp; // We can use Date/Timestamp for timestamp (07-JAN-2019) instead String
	@JsonProperty("TOTALTRADES")
	private double totalTrades;
	@JsonProperty("ISIN")
	private String isin;

}

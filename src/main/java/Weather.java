import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.text.DecimalFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(includeFieldNames = false)

public class Weather {

    private Double temperature;


    private Double clouds;


    private Double pressure;

    private Double humidity;
    private String data;

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return "\nData = '" + data +
                ", Temperatura=" + temperature + " \u00b0C"+
                ", Zachmurzenie =" + clouds + " %" +
                ", Ciśnienie =" + pressure + " hPa "+
                ", Wilgotność =" + humidity +" % "+
                '\'';

    }
}
package dto;

import com.cmc.moengagedataimport.entities.SbfLoanPortfolio;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ResourceDto implements Serializable {
    Map<String, List<SbfLoanPortfolio>> dataImport;
}

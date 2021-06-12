package io.aktivator.campaign.donation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.aktivator.campaign.CampaignStatus;
import io.aktivator.configuration.UserIdSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DonationDto {
  private final Long id;
  private final String title;
  private final String description;

  @JsonSerialize(converter = UserIdSerializer.class)
  private final Long ownerId;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private final Date startDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private final Date endDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private final Date created;

  private final Long target;
  private final boolean featured;
  private final boolean liked;
  private final int likesCount;
  private final CampaignStatus campaignStatus;
  private final BigDecimal balance;
}

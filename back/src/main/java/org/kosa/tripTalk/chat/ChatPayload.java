package org.kosa.tripTalk.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatPayload {

  private Long receiverId;
  private String message;
  
  
}

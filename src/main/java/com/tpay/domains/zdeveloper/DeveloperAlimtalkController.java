package com.tpay.domains.zdeveloper;

import com.tpay.domains.alimtalk.domain.AlimTalkTemplate;
import com.tpay.domains.alimtalk.domain.dto.AlimTalkApiDto;
import com.tpay.domains.alimtalk.presentation.AlimTalkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/dev")
public class DeveloperAlimtalkController {

    private final AlimTalkService alimTalkService;

    @GetMapping("/alimtalk/{dayOfWeek}/{hour}")
    public void alimtalkTest(@PathVariable String dayOfWeek, @PathVariable String hour) {
        DayOfWeek dow = DayOfWeek.valueOf(dayOfWeek);
        AlimTalkTemplate alimTalkTemplate = getAlimtalkTemplateForTest(dow, Integer.parseInt(hour));
        AlimTalkApiDto.Request message
                = alimTalkService.createAlimtalkMessageForTest("테스트상점", alimTalkTemplate, "01082692671");
        alimTalkService.sendAlimTalkApiMessage(message);
    }

    private AlimTalkTemplate getAlimtalkTemplateForTest(DayOfWeek dayOfWeek, int hour) {
        if (dayOfWeek.getValue() < 6) {
            // 평일
            if (hour < 17) {
                // 17시 이전
                return AlimTalkTemplate.SJT_085829;
            } else {
                // 17시 이후
                if(DayOfWeek.FRIDAY == dayOfWeek) {
                    return AlimTalkTemplate.SJT_085721;
                }
                return AlimTalkTemplate.SJT_085686;
            }
        } else {
            // 주말
            return AlimTalkTemplate.SJT_085721;
        }
    }
}

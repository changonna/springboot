package hello.hellospring.service;

import hello.hellospring.repository.MemberRepository;
import hello.hellospring.vo.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest // 스프링 컨테이너와 테스트를 함께 실행
@Transactional // 테스트 시작전에 트랜잭션 실행, 테스트 완료 후 항상 롤백
public class MemberServiceIntegrationTest {
    // Test는 다른곳에서 가져다 쓸 용도로 만드는 것이 아니고
    // 이곳에서만 쓰기 때문에 간단하게 필드 주입을 사용해도 된다.
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        // Given
        Member member = new Member();
        member.setName("hello");

        // When
        Long saveId = memberService.join(member);

        // Then
        Member findMember = memberRepository.findById(saveId).get();
        assertEquals(member.getName(), findMember.getName());
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        // Given
        Member member1 = new Member();
        member1.setName("spring1");

        Member member2 = new Member();
        member2.setName("spring1");

        // When
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    }

}

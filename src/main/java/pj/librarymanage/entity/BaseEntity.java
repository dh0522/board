package pj.librarymanage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    // 시간 정보를 다루는 클래스

    @CreationTimestamp // 생성될 때 시간
    @Column(updatable = false) // 수정안돼
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(insertable = false) // 입력될때 안돼
    private LocalDateTime updatedTime;

}

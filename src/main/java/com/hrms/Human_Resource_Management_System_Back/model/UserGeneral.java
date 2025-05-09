package com.hrms.Human_Resource_Management_System_Back.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;


@Entity
@Table(name = "user_general", schema = "public")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGeneral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_general_id")
    private Integer userGeneralId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "profile_photo")
    private byte[] profilePhoto;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "cv", nullable = true)
    private byte[] cvData;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "portfolio")
    private byte[] portfolio;

    @Column(nullable = false)
    private boolean verified;

}



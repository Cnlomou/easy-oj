package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Cnlomou
 * @create 2020/7/5 17:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "oj_languages")
public class OjLanguages {
    @Id
    @Column(name = "language_id")
    private Integer languageId;

    /**
     * 描述
     */
    @Column(name = "language_des")
    private String languageDes;

    /**
     * 名字
     */
    @Column(name = "language_name")
    private String languageName;

    /**
     * 编译命令
     */
    @Column(name = "language_compile_command")
    private String languageCompileCommand;

    /**
     * 运行命令
     */
    @Column(name = "language_run_command")
    private String languageRunCommand;
}
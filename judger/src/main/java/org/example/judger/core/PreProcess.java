package org.example.judger.core;

import lombok.extern.slf4j.Slf4j;
import org.example.bean.SubmissionMsg;
import org.example.judger.config.OjProperties;
import org.example.judger.util.IOUtil;
import org.example.judger.util.RandomString;
import org.example.mapper.OjProblemCheckpointsMapper;
import org.example.model.OjLanguages;
import org.example.model.OjProblemCheckpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Cnlomou
 * @create 2020/7/11 13:47
 *
 * 预处理
 */
@Component
@Slf4j
public class PreProcess {

    @Autowired
    private OjProblemCheckpointsMapper ojProblemCheckpointsMapper;

    /**
     * 编译前的预处理
     * @param msg
     * @param ojProperties
     * @return
     */
    public String preCompilerHandler(SubmissionMsg msg,OjProperties ojProperties){
        try {
            return buildCodeFile(msg,ojProperties);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 运行前的预处理
     * @param msg
     * @param ojProperties
     * @return
     */
    public List<OjProblemCheckpoints> preRuntimeHandler(SubmissionMsg msg, OjProperties ojProperties){
        try {
            return buildCheckPointFile(msg,ojProperties);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 生成测试点文件
     * @param msg
     * @param ojProperties
     */
    private List<OjProblemCheckpoints> buildCheckPointFile(SubmissionMsg msg, OjProperties ojProperties) throws IOException {

        List<OjProblemCheckpoints> checkpoints = ojProblemCheckpointsMapper.selectByExample(
                Example.builder(OjProblemCheckpoints.class)
                        .andWhere(Sqls.custom().andEqualTo("problemId", msg.getProblemId()))
                        .build());

        File workPath=new File(ojProperties.getWorkPath());
        for (OjProblemCheckpoints checkpoint : checkpoints) {
            File checkPointInputFile = new File(workPath, String.format("%s#input#%s#smp.txt",
                    checkpoint.getProblemId(), checkpoint.getProblemCheckpointId()));
            File checkPointOutPutFile=new File(workPath,String.format("%s#output#%s#smp.txt",
                    checkpoint.getProblemId(),checkpoint.getProblemCheckpointId()));
            IOUtil.copy(checkPointInputFile,checkpoint.getProblemCheckpointInput());
            IOUtil.copy(checkPointOutPutFile,checkpoint.getProblemCheckpointOutput());
        }

        return checkpoints;
    }

    /**
     * 生成代码文件
     * @param msg
     * @param ojProperties
     * return 源文件的文件名
     */
    private String buildCodeFile(SubmissionMsg msg, OjProperties ojProperties) throws IOException {
        ojProperties.getWorkPath();
        File workPath = new File(ojProperties.getWorkPath());

        String fileName = String.format("src#%s." + getCodeFileSuffix(msg.getLanguage()),
                RandomString.randomString(10));
        File file = new File(workPath, fileName);
        if (workPath.exists()||workPath.mkdirs()) {
            setWorkPathPermission(workPath);
            IOUtil.copy(file,msg.getCode());
        }
        return file.getPath();
    }

    /**
     * 设置工作路径的权限
     * @param workPath
     */
    private void setWorkPathPermission(File workPath) throws IOException {
        if ( !System.getProperty("os.name").contains("Windows") ) {
            Set<PosixFilePermission> permissions = new HashSet<>();

            permissions.add(PosixFilePermission.OWNER_READ);
            permissions.add(PosixFilePermission.OWNER_WRITE);
            permissions.add(PosixFilePermission.OWNER_EXECUTE);

            permissions.add(PosixFilePermission.GROUP_READ);
            permissions.add(PosixFilePermission.GROUP_WRITE);
            permissions.add(PosixFilePermission.GROUP_EXECUTE);

            permissions.add(PosixFilePermission.OTHERS_READ);
            permissions.add(PosixFilePermission.OTHERS_WRITE);
            permissions.add(PosixFilePermission.OTHERS_EXECUTE);
            Files.setPosixFilePermissions(workPath.toPath(), permissions);
        }
    }

    /**
     * 得到源代码的后缀名
     * @param language
     * @return
     */
    private String getCodeFileSuffix(OjLanguages language) {
        String compileCommand = language.getLanguageCompileCommand();

        Pattern pattern = Pattern.compile("\\{filename\\}\\.((?!exe| ).)+");
        Matcher matcher = pattern.matcher(compileCommand);

        if ( matcher.find() ) {
            String sourceFileName = matcher.group();
            return sourceFileName.replaceAll("\\{filename\\}\\.", "");
        }
        return "";
    }
}

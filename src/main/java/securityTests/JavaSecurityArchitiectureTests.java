package securityTests;

import java.net.SocketPermission;

/**
 * @author JASONJ
 * @date 2022/1/11
 * @time 22:57
 * @description  java security architecture
 **/
public class JavaSecurityArchitiectureTests {

    public static void main(String[] args) {

        SecurityManager securityManager = new SecurityManager();

        System.setSecurityManager(securityManager);

        SocketPermission permission = new SocketPermission("","accept");

        securityManager.checkPermission(permission);


//        ProtectionDomain protectionDomain = JavaSecurityArchitiectureTests.class.getProtectionDomain();
//        System.out.println(Arrays.toString(protectionDomain.getPrincipals()));
//
//        CodeSource codeSource = protectionDomain.getCodeSource();
//        System.out.println(Arrays.toString(codeSource.getCertificates()));
//        System.out.println(Arrays.toString(codeSource.getCodeSigners()));
//        System.out.println(codeSource.getLocation());


    }

}

package pattern;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternTests {
    public static void main(String[] args) {
        String pattern = "(\\w|[,'&\"])+";
        String text = "sfklsdjfkl,ksldfj&sdlkf\"";
        System.out.println(text.matches(pattern));

        System.out.println(Pattern.matches(pattern, text));

        String simple = "123";

        System.out.println(simple.matches("[123]+"));

        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher("1233,&23234234\"");
        boolean b = matcher.find();
        System.out.println(b);

        System.out.println(matcher.start());
        System.out.println(matcher.end());
    }
}

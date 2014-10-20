package com.paxcel.trial;

import org.jrubyparser.CompatVersion;
import org.jrubyparser.Parser;
import org.jrubyparser.ast.*;
import org.jrubyparser.parser.ParserConfiguration;
import java.io.StringReader;

public class jRubyTrial {

    public static void main(String[] args) {
        String codeString = "config.vm.define :PuppetMaster do |master|\n master.vm.hostname = \"puppet.vagrant.chaikinanalytics.com\"	\nend";
        Node node = parseContents(codeString);
        System.out.println(node);
    }

    public static Node parseContents(String string) {
        Parser rubyParser = new Parser();
        StringReader in = new StringReader(string);
        CompatVersion version = CompatVersion.RUBY1_8;
        ParserConfiguration config = new ParserConfiguration(0, version);
        return rubyParser.parse("<code>", in, config);
    }
}
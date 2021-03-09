package skillapi.api.processor;

import com.google.auto.service.AutoService;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.*;
import skillapi.api.annotation.SkillPacket;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * @author Jun
 * @date 2020/8/31.
 */
@SupportedAnnotationTypes("skillapi.api.annotation.SkillPacket")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class SkillPacketAnnotationProcessor extends AbstractProcessor {
    private Messager messager;
    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;

    private Name constructorName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
        this.constructorName = names.fromString("<init>");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(SkillPacket.class);
        for (Element element : set) {
            if (element.getKind() != ElementKind.CLASS) {
                error(element, "Only classes can be annotated with @%s", SkillPacket.class.getSimpleName());
                // exit
                return true;
            }

            JCTree jcTree = trees.getTree(element);
            jcTree.accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
                    List<JCTree.JCVariableDecl> jcVariableDeclList = List.nil();
                    boolean hasConstructor = false;
                    // Get class fields
                    for (JCTree tree : jcClassDecl.defs) {
                        if (tree.getKind().equals(Tree.Kind.VARIABLE)) {
                            JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) tree;
                            jcVariableDeclList = jcVariableDeclList.append(jcVariableDecl);
                        }
                        if (tree.getKind().equals(Tree.Kind.METHOD)) {
                            JCTree.JCMethodDecl jcMethodDecl = (JCTree.JCMethodDecl) tree;
                            if (jcMethodDecl.name.equals(constructorName) && jcMethodDecl.params.length() == 0) {
                                hasConstructor = true;
                            }
                        }
                    }

                    // Inject getter & setter
                    for (JCTree.JCVariableDecl jcVariableDecl : jcVariableDeclList) {
                        jcClassDecl.defs = jcClassDecl.defs
                                .prepend(createGetter(jcVariableDecl))
                                .prepend(createSetter(jcVariableDecl));
                    }

                    if (!hasConstructor) {
                        jcClassDecl.defs = jcClassDecl.defs.prepend(createNoArgsConstructor());
                    }
                    super.visitClassDef(jcClassDecl);
                }
            });
        }
        return true;
    }

    private JCTree.JCMethodDecl createNoArgsConstructor() {
        JCTree.JCBlock body = treeMaker.Block(0, List.nil());
        return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PRIVATE),
                constructorName,
                null,
                List.nil(),
                List.nil(),
                List.nil(),
                body,
                null
        );
    }

    private JCTree.JCMethodDecl createGetter(JCTree.JCVariableDecl jcVariableDecl) {
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        statements.append(treeMaker.Return(treeMaker.Select(treeMaker.Ident(names.fromString("this")), jcVariableDecl.getName())));
        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
        return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC),
                getNewMethodName("get", jcVariableDecl.getName()),
                jcVariableDecl.vartype,
                List.nil(),
                List.nil(),
                List.nil(),
                body,
                null
        );
    }

    private JCTree.JCMethodDecl createSetter(JCTree.JCVariableDecl jcVariableDecl) {
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();

        statements.append(treeMaker.Exec(
                treeMaker.Assign(
                        treeMaker.Select(treeMaker.Ident(names.fromString("this")), jcVariableDecl.getName()),
                        treeMaker.Ident(jcVariableDecl.getName())
                )
        ));
        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());

        JCTree.JCVariableDecl param = treeMaker.VarDef(
                treeMaker.Modifiers(Flags.PARAMETER),
                names.fromString(jcVariableDecl.getName().toString()),
                jcVariableDecl.vartype,
                null
        );
        List<JCTree.JCVariableDecl> parameters = List.of(param);

        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC),
                getNewMethodName("set", jcVariableDecl.getName()),
                treeMaker.Type(new Type.JCVoidType()),
                List.nil(),
                parameters,
                List.nil(),
                body,
                null
        );
    }

    private Name getNewMethodName(String prefix, Name name) {
        String s = name.toString();
        return names.fromString(prefix + s.substring(0, 1).toUpperCase() + s.substring(1, name.length()));
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }
}
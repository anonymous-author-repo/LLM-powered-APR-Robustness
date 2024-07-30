import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ForToWhileLoopModifier extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(ForStmt n, Void arg) {
        super.visit(n, arg);

        // ����whileѭ����������Ĭ��Ϊtrue
        Expression compare = n.getCompare().orElse(new BooleanLiteralExpr(true));

        // ����whileѭ�����壬����ԭforѭ����͸��±��ʽ
        BlockStmt body = new BlockStmt();

        // ��ԭforѭ������ӵ��µ�whileѭ������
        n.getBody().ifBlockStmt(b -> body.getStatements().addAll(b.getStatements()));
        // �����±��ʽ��ӵ�whileѭ�����ĩβ
        n.getUpdate().forEach(update -> body.addStatement(new ExpressionStmt(update)));

        // �����µ�whileѭ��
        WhileStmt whileStmt = new WhileStmt(compare, body);

        BlockStmt parentBlock = (BlockStmt) n.getParentNode().orElse(null);
        if (parentBlock == null) {
            return; // ȷ�����ڵ���BlockStmt
        }

        // ����forѭ���ڸ�BlockStmt�е�λ��
        int index = parentBlock.getStatements().indexOf(n);

        // ����һ���µ�NodeList���洢�޸ĺ�����
        NodeList<Statement> modifiedStatements = new NodeList<>();

        // ���ԭforѭ��֮ǰ��������䵽modifiedStatements
        modifiedStatements.addAll(parentBlock.getStatements().subList(0, index));

        // ���ԭforѭ���г�ʼ�����ʽ��������Ϊ����������
        n.getInitialization().forEach(init -> modifiedStatements.add(new ExpressionStmt(init)));

        // ����¹����whileѭ����modifiedStatements
        modifiedStatements.add(whileStmt);

        // ���ԭforѭ��֮���������䵽modifiedStatements
        modifiedStatements.addAll(parentBlock.getStatements().subList(index + 1, parentBlock.getStatements().size()));

        // ʹ���µ�����б��滻ԭ��BlockStmt�е����
        parentBlock.setStatements(modifiedStatements);
    }
}

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class AddDummyVariableModifier extends VoidVisitorAdapter<Void> {
    private int variableCounter = 1; // ��ʼֵ��Ϊ1�����ڱ������ĵ���

    @Override
    public void visit(MethodDeclaration n, Void arg) {
        if (n.getBody().isPresent()) {
            BlockStmt body = n.getBody().get();

            // ʹ�ñ���������������������Ȼ�����������
            String varName = "dummyVar" + variableCounter++;
            VariableDeclarator variableDeclarator = new VariableDeclarator(
                PrimitiveType.intType(), varName, new IntegerLiteralExpr("0"));
            VariableDeclarationExpr dummyVarExpr = new VariableDeclarationExpr(variableDeclarator);

            ExpressionStmt dummyVarStmt = new ExpressionStmt(dummyVarExpr);

            // ���ѡ��һ�������
            int insertIndex = (int) (Math.random() * (body.getStatements().size() + 1));

            // �����λ�ò����µľֲ���������
            body.getStatements().add(insertIndex, dummyVarStmt);
        }
        super.visit(n, arg);
    }
}

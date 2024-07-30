import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class SimpleConditionTransformer extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(BinaryExpr n, Void arg) {
        super.visit(n, arg);

        if (n.getOperator() == BinaryExpr.Operator.GREATER) {
            // ���������Ĳ�����ΪLESS
            Expression left = n.getLeft();
            Expression right = n.getRight();
            n.setLeft(right);
            n.setRight(left);
            n.setOperator(BinaryExpr.Operator.LESS);
        } else if (n.getOperator() == BinaryExpr.Operator.LESS) {
            // ���������Ĳ�����ΪGREATER
            Expression left = n.getLeft();
            Expression right = n.getRight();
            n.setLeft(right);
            n.setRight(left);
            n.setOperator(BinaryExpr.Operator.GREATER);
        } else if (n.getOperator() == BinaryExpr.Operator.AND || n.getOperator() == BinaryExpr.Operator.OR) {
            // Ϊ�������ʽ��Ӷ��������
            EnclosedExpr enclosed = new EnclosedExpr(n.clone());
            n.replace(enclosed);
        }
        // ���ڵ��ںͲ����ڲ������������иı�
    }
}

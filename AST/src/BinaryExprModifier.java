import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;

public class BinaryExprModifier extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(BinaryExpr n, Void arg) {
        super.visit(n, arg);

        // ����Ƿ�Ϊ�ַ������Ӳ���
        if (n.getOperator() == BinaryExpr.Operator.PLUS && (involvesString(n.getLeft()) || involvesString(n.getRight()))) {
            // ����漰�ַ������ӣ��򲻽����Ŷ�
            return;
        }

        switch (n.getOperator()) {
            case PLUS:
            case MULTIPLY:
                // �������Ҳ�����
                Expression temp = n.getLeft();
                n.setLeft(n.getRight());
                n.setRight(temp);
                break;
            case MINUS:
                // �ĳɼ�һ�������෴��
                n.setOperator(BinaryExpr.Operator.PLUS);
                n.setRight(new UnaryExpr(n.getRight(), UnaryExpr.Operator.MINUS));
                break;
            case DIVIDE:
                // �ĳɳ���һ�����ĵ���
                n.setOperator(BinaryExpr.Operator.MULTIPLY);
                EnclosedExpr reciprocal = new EnclosedExpr(new BinaryExpr(new IntegerLiteralExpr(1), n.getRight().clone(), BinaryExpr.Operator.DIVIDE));
                n.setRight(reciprocal);
                break;
            // �������������԰�����Ӹ�����Ŷ��߼�
        }
    }

    private boolean involvesString(Expression expr) {
        // �򵥼�飺������ʽֱ����StringLiteralExpr������Ϊ�漰�ַ���
        // �����ӵ����������Ҫ���ͽ���
        return expr.isStringLiteralExpr();
    }
}

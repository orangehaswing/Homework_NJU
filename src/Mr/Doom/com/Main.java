package Mr.Doom.com;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
	static BinaryTree tree;

	public static void main(String[] args) {
		double EquaResult = 0;
		boolean HasAdd2Tree = false;
		List<String> elements1 = new ArrayList<String>();
		List<String> elements2 = new ArrayList<String>();
		int Numb2elemts = 0;
		int score = 0;

		while (true) {
			// ���빫ʽ����֤�϶���ȷ����ʹ�����ţ�
			// eg��1+2-3*4+5/6 ; 9-8+7*6/5
			System.out.println("�����빫ʽ ��");
			Scanner input = new Scanner(System.in);
			String equation = input.next();

			// ������
			try {
				EquaResult = jisuanStr(equation);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(EquaResult);

			// ����һ��������
			BTreeNode Node = new BTreeNode(null);
			tree = new BinaryTree(Node);

			// �ݹ���ã�����ʽ�����������������
			HasAdd2Tree = listRecursion(equation);

			// ����������
			if (Numb2elemts % 2 == 0) {
				elements1 = tree.order(tree.getRoot());
				elements1.add(Double.toString(EquaResult));
			} else {
				elements2 = tree.order(tree.getRoot());
				elements2.add(Double.toString(EquaResult));

				// �Ƚ�����������
				if ((elements1 != null) && (elements2 != null)) {
					score = similarity(elements1, elements2);
				}
				System.out.println("���ƶ���" + score + "%");

			}
			Numb2elemts++;

			// �������������
			// if (Numb2elemts == 2) {
			//
			// for (String str1 : elements1) {
			// System.out.println(str1);
			// }
			// System.out.println("--------------------");
			// for (String str2 : elements2) {
			// System.out.println(str2);
			// }
			// }

			if (!HasAdd2Tree) {
				input.close();
			}
		}
	}

	// �ݹ���ú��� add2Tree
	public static boolean listRecursion(String RecursionString) {
		boolean flag = false;
		String[] StrValue = null;

		if (RecursionString != "") {
			flag = true;
			if ((RecursionString.indexOf("+")) != -1) {
				StrValue = RecursionString.split("\\+", 2);
				tree.insert("+");

				if (StrValue[0].length() == 1) {
					tree.insert(StrValue[0]);
				} else {
					flag = listRecursion(StrValue[0]);
				}

				if (StrValue[1].length() == 1) {
					tree.insert(StrValue[1]);
				} else {
					flag = listRecursion(StrValue[1]);
				}
			} else if ((RecursionString.indexOf("-")) != -1) {
				StrValue = RecursionString.split("\\-", 2);
				tree.insert("-");

				if (StrValue[0].length() == 1) {
					tree.insert(StrValue[0]);
				} else {
					flag = listRecursion(StrValue[0]);
				}

				if (StrValue[1].length() == 1) {
					tree.insert(StrValue[1]);
				} else {
					flag = listRecursion(StrValue[1]);
				}
			} else if ((RecursionString.indexOf("*")) != -1) {
				StrValue = RecursionString.split("\\*", 2);
				tree.insert("*");

				if (StrValue[0].length() == 1) {
					tree.insert(StrValue[0]);
				} else {
					flag = listRecursion(StrValue[0]);
				}

				if (StrValue[1].length() == 1) {
					tree.insert(StrValue[1]);
				} else {
					flag = listRecursion(StrValue[1]);
				}
			} else if ((RecursionString.indexOf("/")) != -1) {
				StrValue = RecursionString.split("\\/", 2);
				tree.insert("/");

				if (StrValue[0].length() == 1) {
					tree.insert(StrValue[0]);
				} else {
					flag = listRecursion(StrValue[0]);
				}

				if (StrValue[1].length() == 1) {
					tree.insert(StrValue[1]);
				} else {
					flag = listRecursion(StrValue[1]);
				}
			}
		} else {
			return flag;
		}

		return flag;
	}

	// 1.���
	// 2.��ʽ����
	// 3.ÿ��Ԫ��
	public static int similarity(List<String> elemt1, List<String> elemt2) {
		int score = 0;
		int number = 0;
		int sumlen = 1;
		// ���
		String TempElemt1 = elemt1.get(elemt1.size() - 1);
		String TempElemt2 = elemt2.get(elemt2.size() - 1);
		sumlen = (elemt1.size() > elemt2.size() ? elemt1.size() : elemt2.size());

		if (TempElemt1.endsWith(TempElemt2)) {
			score += 10;
		}
		// ����
		if (elemt1.size() == elemt2.size()) {
			score += 10;
		}
		// Ԫ����ͬ
		for (int i = 0; i < elemt1.size(); i++) {
			if (elemt2.get(i) != null) {
				// �Ƚ��Ƿ���ͬ
				if (elemt1.get(i).equals(elemt2.get(i))) {
					number++;
				}
			}
		}
		score +=  80 * ((double)number / sumlen);
		return score;
	}

	/**
	 * �����ʽ��ĸ���Ԫ�ز������Ӧ����λ��<br>
	 */
	public static List<String> splitStr(String string) throws Exception {
		List<String> listSplit = new ArrayList<String>();
		Matcher matcher = Pattern.compile("\\-?\\d+(\\.\\d+)?|[*/()]|\\-")
				.matcher(string);// �������ֳ�ÿ��Ԫ��
		while (matcher.find()) {
			// System.out.println(matcher.group(0));
			listSplit.add(matcher.group(0));
		}
		return listSplit;
	}

	/**
	 * ����<br>
	 * ���裺1�����������<br>
	 * Ȼ��ȡ��һ�������(���� ���㵱ǰ������������ʽ �����ڼ������²������� �Դ�����,ֱ��ѭ��ʹ�õ���������Ԫ��
	 * ������ϣ�����˳�����š��˳����Ӽ���
	 */
	public static double jisuanStr(String str) throws Exception {
		double returnDouble = 0;
		List<String> listSplit = splitStr(str); // ��ֺõ�Ԫ��
		List<Integer> zKuohaoIdxList = new ArrayList<Integer>();// ������,<�������꣬>
		if (Pattern.compile(".*\\(|\\).*").matcher(str).find()) {// ���������������
			String value = "";// �����ַ�ֵ
			int zIdx = 0;// ��һ����������zKuoHaoIdxList���±�
			// �˲�ѭ���������������������ʽ
			List<String> tempList = new ArrayList<String>();// ǰ��û�м����Ԫ��
			int removeL = 0;
			int tempListSize = 0;
			for (int i = 0; i < listSplit.size(); i++) {
				value = listSplit.get(i);
				tempList.add(value);
				tempListSize = tempList.size();
				if ("(".equals(value)) {// ������
					zKuohaoIdxList.add(tempListSize - 1);
				} else if (")".equals(value)) {// ���������žͼ�������һ�����ż����ʽ
					zIdx = zKuohaoIdxList.size() - 1;// �뵱ǰ��������������������
					int start = zKuohaoIdxList.get(zIdx);
					returnDouble = jisuan(tempList, start + 1, tempListSize - 1); // ��ʼλ��,������һ��������
					removeL = tempListSize - start;
					tempList = removeUseList(tempList, removeL);// �Ƴ���ʹ�õ�Ԫ��
					tempList.add(returnDouble + "");// �ոռ����ֵ��ӽ���
					zKuohaoIdxList.remove(zIdx);// ��������������
				}
			}
			// �����м�����
			returnDouble = jisuan(tempList, 0, tempList.size());
		} else {// û����������
			returnDouble = jisuan(listSplit, 0, listSplit.size());
		}
		return returnDouble;
	}

	/**
	 * ����ɾ�����ù���Ԫ��
	 */
	public static List<String> removeUseList(List<String> list, int removeLength) {
		int le = list.size() - removeLength;
		for (int i = list.size() - 1; i >= le; i--) {
			list.remove(i);
		}
		return list;
	}

	/**
	 * ������ʽ
	 */
	public static double jisuan(List<String> listSplit, int start, int end)
			throws Exception {
		double returnValue = 0;
		String strValue = null;// ��ʱ����
		List<String> jjValueList = new ArrayList<String>();// ʣ�µļӼ�Ԫ��
		// ��������˳���
		for (int i = start; i < end; i++) {
			strValue = listSplit.get(i);
			if ("*".equals(strValue) || "/".equals(strValue)) {// �˳�
				strValue = jisuanValue(
						"*".equals(strValue) ? "*" : "/",
						Double.parseDouble(jjValueList.get(jjValueList.size() - 1)),
						Double.parseDouble(listSplit.get(i + 1)))
						+ "";
				jjValueList.remove(jjValueList.size() - 1);
				i++;
			}
			jjValueList.add(strValue);
		}
		// ��������Ӽ�
		for (int j = 0; j < jjValueList.size(); j++) {
			strValue = jjValueList.get(j);
			if ("-".equals(strValue) || "+".equals(strValue)) {
				returnValue = jisuanValue("-".equals(strValue) ? "-" : "+",
						returnValue, Double.parseDouble(jjValueList.get(j + 1)));
				j++;
			} else {
				returnValue += Double.parseDouble(jjValueList.get(j));
			}
		}
		return returnValue;
	}

	/**
	 * ����2������ļӼ��˳����� �磺2*5 ��2/5
	 */
	public static double jisuanValue(String type, double start, double end)
			throws Exception {
		double d = 0;
		if ("-".equals(type)) {
			d = start - end;
		} else if ("+".equals(type)) {
			d = start + end;
		} else if ("*".equals(type)) {
			d = start * end;
		} else if ("/".equals(type)) {
			if (0 == start || 0 == end)
				d = 0;
			else
				d = start / end;
		}
		return d;
	}

}

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
			// 输入公式，保证肯定正确，不使用括号，
			// eg：1+2-3*4+5/6 ; 9-8+7*6/5
			System.out.println("请输入公式 ：");
			Scanner input = new Scanner(System.in);
			String equation = input.next();

			// 计算结果
			try {
				EquaResult = jisuanStr(equation);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(EquaResult);

			// 创建一个二叉树
			BTreeNode Node = new BTreeNode(null);
			tree = new BinaryTree(Node);

			// 递归调用，将算式按算术符插入二叉树
			HasAdd2Tree = listRecursion(equation);

			// 遍历二叉树
			if (Numb2elemts % 2 == 0) {
				elements1 = tree.order(tree.getRoot());
				elements1.add(Double.toString(EquaResult));
			} else {
				elements2 = tree.order(tree.getRoot());
				elements2.add(Double.toString(EquaResult));

				// 比较两个二叉树
				if ((elements1 != null) && (elements2 != null)) {
					score = similarity(elements1, elements2);
				}
				System.out.println("相似度是" + score + "%");

			}
			Numb2elemts++;

			// 输出两个二叉树
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

	// 递归调用函数 add2Tree
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

	// 1.结果
	// 2.算式长度
	// 3.每个元素
	public static int similarity(List<String> elemt1, List<String> elemt2) {
		int score = 0;
		int number = 0;
		int sumlen = 1;
		// 结果
		String TempElemt1 = elemt1.get(elemt1.size() - 1);
		String TempElemt2 = elemt2.get(elemt2.size() - 1);
		sumlen = (elemt1.size() > elemt2.size() ? elemt1.size() : elemt2.size());

		if (TempElemt1.endsWith(TempElemt2)) {
			score += 10;
		}
		// 长度
		if (elemt1.size() == elemt2.size()) {
			score += 10;
		}
		// 元素相同
		for (int i = 0; i < elemt1.size(); i++) {
			if (elemt2.get(i) != null) {
				// 比较是否相同
				if (elemt1.get(i).equals(elemt2.get(i))) {
					number++;
				}
			}
		}
		score +=  80 * ((double)number / sumlen);
		return score;
	}

	/**
	 * 拆分算式里的各个元素并处理对应所在位置<br>
	 */
	public static List<String> splitStr(String string) throws Exception {
		List<String> listSplit = new ArrayList<String>();
		Matcher matcher = Pattern.compile("\\-?\\d+(\\.\\d+)?|[*/()]|\\-")
				.matcher(string);// 用正则拆分成每个元素
		while (matcher.find()) {
			// System.out.println(matcher.group(0));
			listSplit.add(matcher.group(0));
		}
		return listSplit;
	}

	/**
	 * 计算<br>
	 * 步骤：1、如果有括号<br>
	 * 然后取上一个最近的(坐标 计算当前括号组合里的算式 ），在继续往下查找括号 以此类推,直至循环使用到所有坐标元素
	 * 计算完毕（运算顺序括号、乘除、加减）
	 */
	public static double jisuanStr(String str) throws Exception {
		double returnDouble = 0;
		List<String> listSplit = splitStr(str); // 拆分好的元素
		List<Integer> zKuohaoIdxList = new ArrayList<Integer>();// 左括号,<所在坐标，>
		if (Pattern.compile(".*\\(|\\).*").matcher(str).find()) {// 如果包含括号运算
			String value = "";// 单个字符值
			int zIdx = 0;// 上一个左括号在zKuoHaoIdxList的下标
			// 此层循环计算完所有括号里的算式
			List<String> tempList = new ArrayList<String>();// 前面没有计算的元素
			int removeL = 0;
			int tempListSize = 0;
			for (int i = 0; i < listSplit.size(); i++) {
				value = listSplit.get(i);
				tempList.add(value);
				tempListSize = tempList.size();
				if ("(".equals(value)) {// 左括号
					zKuohaoIdxList.add(tempListSize - 1);
				} else if (")".equals(value)) {// 遇到右括号就计算与上一左括号间的算式
					zIdx = zKuohaoIdxList.size() - 1;// 离当前右括号最近的左括号配对
					int start = zKuohaoIdxList.get(zIdx);
					returnDouble = jisuan(tempList, start + 1, tempListSize - 1); // 开始位置,就是上一个左括号
					removeL = tempListSize - start;
					tempList = removeUseList(tempList, removeL);// 移除已使用的元素
					tempList.add(returnDouble + "");// 刚刚计算的值添加进来
					zKuohaoIdxList.remove(zIdx);// 计算完毕清除括号
				}
			}
			// 把所有计算完
			returnDouble = jisuan(tempList, 0, tempList.size());
		} else {// 没有括号运算
			returnDouble = jisuan(listSplit, 0, listSplit.size());
		}
		return returnDouble;
	}

	/**
	 * 倒序删除已用过的元素
	 */
	public static List<String> removeUseList(List<String> list, int removeLength) {
		int le = list.size() - removeLength;
		for (int i = list.size() - 1; i >= le; i--) {
			list.remove(i);
		}
		return list;
	}

	/**
	 * 计算算式
	 */
	public static double jisuan(List<String> listSplit, int start, int end)
			throws Exception {
		double returnValue = 0;
		String strValue = null;// 临时变量
		List<String> jjValueList = new ArrayList<String>();// 剩下的加减元素
		// 遍历计算乘除法
		for (int i = start; i < end; i++) {
			strValue = listSplit.get(i);
			if ("*".equals(strValue) || "/".equals(strValue)) {// 乘除
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
		// 遍历计算加减
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
	 * 计算2个数间的加减乘除操作 如：2*5 ，2/5
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

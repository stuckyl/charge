package com.charge.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * Created by liuxm on 2018/4/9.
 */
public class Utils {
	/**
	 * 计算年终奖个调税
	 *
	 * @param money
	 * @return
	 */
	public static double calculateYearEndBonusesIncomeTax(double money) {
		if (money > 0) {
			double monthMoney = money / 12;

			if (monthMoney > 80000) {
				monthMoney = monthMoney * 0.45 * 12 - 13505;
			} else if (monthMoney > 55000) {
				monthMoney = monthMoney * 0.35 * 12 - 5505;
			} else if (monthMoney > 35000) {
				monthMoney = monthMoney * 0.30 * 12 - 2755;
			} else if (monthMoney > 9000) {
				monthMoney = monthMoney * 0.25 * 12 - 1005;
			} else if (monthMoney > 4500) {
				monthMoney = monthMoney * 0.20 * 12 - 555;
			} else if (monthMoney > 1500) {
				monthMoney = monthMoney * 0.1 * 12 - 105;
			} else {
				monthMoney = monthMoney * 0.03 * 12;
			}

			return new BigDecimal(monthMoney).setScale(2, RoundingMode.UP).doubleValue();
		}

		return 0.00;
	}

	/**
	 * 计算新版年终奖个调税
	 *
	 * @param money
	 * @return
	 */
	public static double calculateNewYearEndBonusesIncomeTax(double money) {
		if (money > 0) {
			double monthMoney = money / 12;
			if (monthMoney > 80000) {
				monthMoney = monthMoney * 0.45 * 12 - 15160;
			} else if (monthMoney > 55000) {
				monthMoney = monthMoney * 0.35 * 12 - 7160;
			} else if (monthMoney > 35000) {
				monthMoney = monthMoney * 0.30 * 12 - 4410;
			} else if (monthMoney > 25000) {
				monthMoney = monthMoney * 0.25 * 12 - 2660;
			} else if (monthMoney > 12000) {
				monthMoney = monthMoney * 0.20 * 12 - 1410;
			} else if (monthMoney > 3000) {
				monthMoney = monthMoney * 0.1 * 12 - 210;
			} else {
				monthMoney = monthMoney * 0.03 * 12;
			}

			return new BigDecimal(monthMoney).setScale(2, RoundingMode.UP).doubleValue();
		}

		return 0.00;
	}

	/**
	 * 计算工资绩效个调税 个税免征额是3500
	 *
	 * @param money
	 * @return
	 */
	public static double calculateIndividualIncomeTax(double money) {
		if (money > 0) {
			money = money - 3500;

			if (money > 0) {
				if (money > 80000) {
					money = money * 0.45 - 13505;
				} else if (money > 55000) {
					money = money * 0.35 - 5505;
				} else if (money > 35000) {
					money = money * 0.30 - 2755;
				} else if (money > 9000) {
					money = money * 0.25 - 1005;
				} else if (money > 4500) {
					money = money * 0.2 - 555;
				} else if (money > 1500) {
					money = money * 0.1 - 105;
				} else {
					money = money * 0.03;
				}

				return new BigDecimal(money).setScale(2, RoundingMode.UP).doubleValue();
			}
		}

		return 0.00;
	}

	/**
	 * 计算新版工资绩效个调税 个税免征额是5000
	 *
	 * @param money
	 * @return
	 */
	public static double calculateNewIndividualIncomeTax(double money) {
		if (money > 0) {
			money = money - 5000;

			if (money > 0) {
				if (money > 80000) {
					money = money * 0.45 - 15160;
				} else if (money > 55000) {
					money = money * 0.35 - 7160;
				} else if (money > 35000) {
					money = money * 0.30 - 4410;
				} else if (money > 25000) {
					money = money * 0.25 - 2660;
				} else if (money > 12000) {
					money = money * 0.2 - 1410;
				} else if (money > 3000) {
					money = money * 0.1 - 210;
				} else {
					money = money * 0.03;
				}

				return new BigDecimal(money).setScale(2, RoundingMode.UP).doubleValue();
			}
		}

		return 0.00;
	}

	/**
	 * 计算工资绩效个调税
	 *
	 * @param money
	 * @return
	 */
	public static double calculateIndividualIncomeTax(double money, double baseMoney) {
		if (money > 0) {
			money = money - baseMoney;

			if (money > 0) {
				if (money > 80000) {
					money = money * 0.45 - 13505;
				} else if (money > 55000) {
					money = money * 0.35 - 5505;
				} else if (money > 35000) {
					money = money * 0.30 - 2755;
				} else if (money > 9000) {
					money = money * 0.25 - 1005;
				} else if (money > 4500) {
					money = money * 0.2 - 555;
				} else if (money > 1500) {
					money = money * 0.1 - 105;
				} else {
					money = money * 0.03;
				}
				return money;
				/*
				 * return new BigDecimal(money).setScale(2,
				 * RoundingMode.UP).doubleValue();
				 */
			}
		}

		return 0.00;
	}

	/**
	 * 计算新版工资绩效个调税
	 *
	 * @param money
	 * @return
	 */
	public static double calculateNewIndividualIncomeTax(double money, double baseMoney) {
		if (money > 0) {
			money = money - baseMoney;

			if (money > 0) {
				if (money > 80000) {
					money = money * 0.45 - 15160;
				} else if (money > 55000) {
					money = money * 0.35 - 7160;
				} else if (money > 35000) {
					money = money * 0.30 - 4410;
				} else if (money > 25000) {
					money = money * 0.25 - 2660;
				} else if (money > 12000) {
					money = money * 0.2 - 1410;
				} else if (money > 3000) {
					money = money * 0.1 - 210;
				} else {
					money = money * 0.03;
				}
				return money;
				/*
				 * return new BigDecimal(money).setScale(2,
				 * RoundingMode.UP).doubleValue();
				 */
			}
		}

		return 0.00;
	}

	/**
	 * 生成主键
	 *
	 * @return
	 */
	public static Long generateId() {
		// 当前系统时间戳精确到毫秒
		Long simple = System.currentTimeMillis();
		// 三位随机数
		int random = new Random().nextInt(900) + 100;
		return Long.parseLong(simple.toString() + random);
	}
}

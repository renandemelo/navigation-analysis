package org.navigationanalysis.statistics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import org.navigationanalysis.navigation.Navigation;

public class StatisticsRecord {

	private Navigation navigation;
	private String sourceFile;
	private Date date;
	private String site;
	private String student;
	private double time;
	private BigDecimal upTotal;
	private BigDecimal upAvg;
	private BigDecimal upSd;
	private BigDecimal upCoefOfVariation;
	private BigDecimal upPerMinute;
	private BigDecimal upCorrelation;
	private BigDecimal downTotal;
	private BigDecimal downAvg;
	private BigDecimal downSd;
	private BigDecimal downCoefOfVariation;
	private BigDecimal downCorrelation;
	private BigDecimal downPerMinute;

	public StatisticsRecord(String sourceFile) throws IOException {
		this.sourceFile = sourceFile;
		navigation = new Navigation(sourceFile);
		
		long[] downloadPerInterval = navigation.getDownloadBytesPerInterval();
		long[] uploadPerInterval = navigation.getUploadBytesPerInterval();
		
		date = navigation.getDate();
		site = getSiteBy(sourceFile);
		student = getStudent(sourceFile);
		time = navigation.getLast().getInterval(1);
		
		upTotal = Kb(getSum(uploadPerInterval));
		upAvg = Kb(getAverage(uploadPerInterval));
		upSd = Kb(getStandardDeviation(uploadPerInterval));
		upCoefOfVariation = getCoefficientOfVariation(uploadPerInterval);
		upPerMinute = upTotal.divide(minute(time),BigDecimal.ROUND_HALF_UP);
		upCorrelation = getCorrelation(uploadPerInterval);
		
		downTotal = Kb(getSum(downloadPerInterval));
		downAvg = Kb(getAverage(downloadPerInterval));
		downSd = Kb(getStandardDeviation(downloadPerInterval));
		downCoefOfVariation = getCoefficientOfVariation(downloadPerInterval);
		downCorrelation = getCorrelation(downloadPerInterval);		
		downPerMinute = downTotal.divide(minute(time),BigDecimal.ROUND_HALF_UP);		
		
		FileWriter writer = getDetailsFileFor(sourceFile);
		writer.write("Interval,Upload,Download\n");
		for (int i = 0; i < downloadPerInterval.length; i++) {
			//,['08/10',6,0]
			writer.write(",['" + (i * 20) + "'," + Kb(uploadPerInterval[i])+ "," + Kb(downloadPerInterval[i]) + "]\n");
		}
		writer.close();		
		
	}

public String basicStatistics(){	
		
		
	NumberFormat f = DecimalFormat.getCurrencyInstance();
	//String retorno = site + "\t" + student + "\t" + time + "\t" + upTotal + "\t" + upPerMinute + "\t" + downTotal + "\t" + downPerMinute;
	String retorno = site + "\t" + upCoefOfVariation + "\t" + upCorrelation.doubleValue() + "\t" + downCoefOfVariation + "\t" + downCorrelation.doubleValue();
	
		
		return retorno;
	}


	private BigDecimal getCorrelation(long[] traffic) {
		long[] time = new long[traffic.length];
		for (int i = 0; i < time.length; i++) {
			time[i] = i*20;
		}
		BigDecimal avgTraffic = getAverage(traffic);
		BigDecimal sdTraffic = getStandardDeviation(traffic);
		sdTraffic.setScale(10,BigDecimal.ROUND_HALF_UP);
		if(sdTraffic.equals(BigDecimal.ZERO))
			return BigDecimal.ZERO;
		BigDecimal avgTime = getAverage(time);
		BigDecimal sdTime = getStandardDeviation(time);
		
		BigDecimal sum = BigDecimal.ZERO;
		for(int i = 0; i < traffic.length;i ++){
			BigDecimal xi = new BigDecimal(traffic[i]).subtract(avgTraffic);
			BigDecimal x = xi.divide(sdTraffic,10,BigDecimal.ROUND_HALF_UP);
			BigDecimal yi = new BigDecimal(time[i]).subtract(avgTime);
			BigDecimal y = yi.divide(sdTime,10,BigDecimal.ROUND_HALF_UP);
			sum = sum.add(x.multiply(y));
		}
		return sum.divide(new BigDecimal(traffic.length).subtract(BigDecimal.ONE),10,BigDecimal.ROUND_HALF_UP);
	}

	private BigDecimal minute(double time) {
		return new BigDecimal(time).divide(new BigDecimal(60),BigDecimal.ROUND_HALF_UP);
	}

	private BigDecimal Kb(BigDecimal val) {
		return val.divide(new BigDecimal(1024),BigDecimal.ROUND_HALF_UP);
	}
	
	private BigDecimal Kb(long l) {
		return Kb(new BigDecimal(l));
	}

	private BigDecimal getCoefficientOfVariation(long[] values) {
		return getStandardDeviation(values).divide(getAverage(values),BigDecimal.ROUND_HALF_UP);
	}

	private String getStudent(String sourceFile) {
		String lower = sourceFile.toLowerCase();
		if(lower.contains("raphael-lira"))
			return "Student A";
		if(lower.contains("eduardo"))
			return "Student B";
		if(lower.contains("daniel"))
			return "Student C";
		if(lower.contains("willian"))
			return "Student D";
		throw new RuntimeException("Unrecognized student for " + sourceFile + "!!");
	}

	private String getSiteBy(String sourceFile) {
		String lower = sourceFile.toLowerCase();
		if(lower.contains("book"))
			return "Facebook";
		if(lower.contains("terra"))
			return "Terra";
		if(lower.contains("kernel"))
			return "Kernel";
		if(lower.contains("tube"))
			return "YouTube";
		throw new RuntimeException("Unrecognized site for " + sourceFile + "!!");
	}

	private FileWriter getDetailsFileFor(String sourceFile) throws IOException {
		String detailsFile = sourceFile.split("\\.pcapng")[0] + "-details.csv";
		return new FileWriter(new File(detailsFile));
	}

	private BigDecimal getStandardDeviation(long[] values) {
		double sd = Math.sqrt(getVariance(values).doubleValue());		
		return new BigDecimal(sd);
	}

	private BigDecimal getVariance(long[] values) {
		BigDecimal avg = getAverage(values);
		BigDecimal diffs = new BigDecimal(0);
		for (long v : values) {
			BigDecimal bd = new BigDecimal(v);
			BigDecimal diff = bd.subtract(avg).pow(2);
			diffs = diffs.add(diff.pow(2));
		}
		BigDecimal n = new BigDecimal(values.length);
		BigDecimal divisor = n.subtract(BigDecimal.ONE);
		if(divisor.equals(BigDecimal.ZERO))
			divisor = BigDecimal.ONE;
		BigDecimal divide = diffs.divide(divisor,BigDecimal.ROUND_HALF_UP);
		return divide;
	}

	private BigDecimal getAverage(long[] values) {
		return getSum(values).divide(new BigDecimal(values.length),BigDecimal.ROUND_HALF_UP);
	}

	private BigDecimal getSum(long[] values) {
		BigDecimal sum = new BigDecimal(0);
		for (long v : values) {
			sum = sum.add(new BigDecimal(v));
		}
		return sum;
	}	
	
	
}

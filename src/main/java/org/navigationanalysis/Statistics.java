package org.navigationanalysis;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Statistics {

	private Navigation navigation;
	private List<String> sourceList = new ArrayList<String>();;

	public Statistics() {
		File baseDir = new File("/home/renan/Dropbox/usp/disciplinas/Redes/mininet/scripts/entrega-aps");
		ArrayDeque<File> deque = new ArrayDeque<File>();
		deque.add(baseDir);
		while (!deque.isEmpty()) {
			File dir = deque.poll();
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					deque.add(file);
				} else {
					String name = file.getAbsolutePath();
					if (name.endsWith("pcapng"))
						sourceList.add(name);
				}
			}
		}

	}
	
	public void run() throws IOException {
		recordStatistics();
	}

	private void recordStatistics() {
		
		System.out.println("Site\tStudent\tDate\tNavigation time\tUpload\tAverage\tStandard deviation\tTotal\tAverage\tStandard deviation");
		for (String sourceFile : sourceList) {
			try {
				navigation = new Navigation(sourceFile);
				long[] downloadPerMinute = navigation.getDownloadBytesPerInterval();
				long[] uploadPerMinute = navigation.getUploadBytesPerInterval();
				
				Date date = navigation.getDate();
				String site = getSiteBy(sourceFile);
				String student = getStudent(sourceFile);;
				double time = navigation.getLast().getInterval(1);
				
				BigDecimal upTotal = getSum(uploadPerMinute);
				BigDecimal upAvg = getAverage(uploadPerMinute);
				BigDecimal upSd = getStandardDeviation(uploadPerMinute);
				BigDecimal downTotal = getSum(downloadPerMinute);
				BigDecimal downAvg = getAverage(downloadPerMinute);
				BigDecimal downSd = getStandardDeviation(downloadPerMinute);
							
				
				System.out.println(site + "\t" + student + "\t" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date) + "\t" + time + "\t" + upTotal + "\t" + upAvg + "\t" + upSd+ "\t"  + downTotal + "\t" + downAvg + "\t" + downSd + "\t" + sourceFile);
				
				FileWriter writer = getDetailsFileFor(sourceFile);
				writer.write("Interval\tDownload\tUpload\n");
				for (int i = 0; i < downloadPerMinute.length; i++) {
					writer.write(i+"\t" + downloadPerMinute[i] + "\t" + uploadPerMinute[i]+ "\n");
				}
				writer.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
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
		String detailsFile = sourceFile.split("\\.")[0] + ".csv";
		return new FileWriter(new File(detailsFile));
	}

	private BigDecimal getStandardDeviation(long[] values) {
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
		double sd = Math.sqrt(diffs.divide(divisor,BigDecimal.ROUND_HALF_UP).doubleValue());
		
		return new BigDecimal(sd);
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

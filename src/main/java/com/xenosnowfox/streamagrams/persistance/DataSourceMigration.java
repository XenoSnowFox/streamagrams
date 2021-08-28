package com.xenosnowfox.streamagrams.persistance;

import com.xenosnowfox.streamagrams.utils.ResourceUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.sql.DataSource;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Logger;

/**
 * Helper class that applies database migrations between versions.
 */
public class DataSourceMigration {

	/**
	 * Logger instance.
	 */
	private static final Logger LOGGER = Logger.getLogger(DataSourceMigration.class.getName());

	/**
	 * Resource folder containing the database migrations to apply to the database.
	 */
	private static final String DDL_RESOURCE_FOLDER = "database/migrations/";

	/**
	 * Hidden Constructor.
	 */
	private DataSourceMigration() {}

	/**
	 * Applies the latest database migrations to the given datasource.
	 *
	 * @param withDataSource
	 * 		DataSource to apply migration to.
	 */
	public static void update(final DataSource withDataSource) {
		final QueryRunner queryRunner = new QueryRunner(withDataSource);
		final int lastMutationId = DataSourceMigration.getLastAppliedMutationId(queryRunner);
		LOGGER.info("Last Database Mutation ID: " + lastMutationId);

		DataSourceMigration.getResourceFolderFiles()
				.stream()
				.filter(DataSourceMigration.getFilePredicate(lastMutationId))
				.sorted(DataSourceMigration.getFileComparator())
				.forEach(DataSourceMigration.getMutationApplicator(queryRunner));
	}

	/**
	 * Returns a list of tables names.
	 *
	 * @param withQueryRunner
	 * 		Query runner.
	 * @return List of table names.
	 */
	private static List<String> showTables(final QueryRunner withQueryRunner) {
		try {
			return withQueryRunner.query("SHOW TABLES;", new ColumnListHandler<>(1));
		} catch (SQLException sqlException) {
			throw new RuntimeException("Unable to retrieve list of tables from the database.", sqlException);
		}
	}

	/**
	 * Returns the identifier of the last applied mutation.
	 *
	 * @param withQueryRunner
	 * 		Query runner.
	 * @return last mutation ID
	 */
	private static int getLastAppliedMutationId(final QueryRunner withQueryRunner) {
		if (!DataSourceMigration.showTables(withQueryRunner)
				.contains("DDLMigration".toUpperCase(Locale.ROOT))) {
			return -1;
		}

		try {
			return withQueryRunner.query(
					"SELECT MAX(DDLMigration.VERSION) FROM DDLMigration;"
					, new ScalarHandler<>()
			);
		} catch (SQLException sqlException) {
			throw new RuntimeException(sqlException);
		}
	}

	/**
	 * Returns a list of mutation files.
	 *
	 * @return Mutation file list.
	 */
	private static List<File> getResourceFolderFiles() {
		return Optional.of(Thread.currentThread())
				.map(Thread::getContextClassLoader)
				.map(classLoader -> classLoader.getResource(DDL_RESOURCE_FOLDER))
				.map(URL::getPath)
				.map(File::new)
				.map(File::listFiles)
				.map(Arrays::asList)
				.orElseThrow();
	}

	/**
	 * Returns a consumer capable of applying a mutation to the given query runner.
	 *
	 * @param withQueryRunner
	 * 		query runner.
	 * @return file consumer.
	 */
	private static Consumer<File> getMutationApplicator(final QueryRunner withQueryRunner) {
		return withFile -> {
			final int mutationId = Integer.parseInt(withFile.getName()
					.split("\\.")[0]);
			LOGGER.info("Applying Mutation ID: " + mutationId);

			final String sql = ResourceUtils.getResourceAsString("/" + DDL_RESOURCE_FOLDER + withFile.getName());

			try {
				withQueryRunner.execute(sql);

				final String insertSql = "INSERT INTO `DDLMigration` (VERSION, DDL, TIMESTAMP_APPLIED)"
						+ " VALUES (?, ?, CURRENT_TIMESTAMP());";
				withQueryRunner.execute(insertSql, mutationId, sql);
			} catch (SQLException sqlException) {
				throw new RuntimeException(sqlException);
			}
		};
	}

	/**
	 * Returns a predicate that checks if a given mutation file needs to be applied to the database.
	 *
	 * @param withLastAppliedMutationId
	 * 		identifier of the last applied mutation.
	 * @return file predicate.
	 */
	private static Predicate<File> getFilePredicate(final int withLastAppliedMutationId) {
		return file -> {
			String s = file.getName()
					.split("\\.")[0];
			return Integer.parseInt(s) > withLastAppliedMutationId;
		};
	}

	/**
	 * Returns a comparator that sorts files in ascending numerical order.
	 *
	 * @return file comparator.
	 */
	private static Comparator<File> getFileComparator() {
		return (withFirstFile, withSecondFile) -> {
			int intA = Optional.of(withFirstFile)
					.map(File::getName)
					.map(m -> m.split("\\.")[0])
					.map(Integer::parseInt)
					.orElseThrow();

			int intB = Optional.of(withSecondFile)
					.map(File::getName)
					.map(m -> m.split("\\.")[0])
					.map(Integer::parseInt)
					.orElseThrow();

			return Integer.compare(intA, intB);
		};
	}
}

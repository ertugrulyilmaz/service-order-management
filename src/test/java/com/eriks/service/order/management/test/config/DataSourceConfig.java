package com.eriks.service.order.management.test.config;

import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.sql2o.Sql2o;
import org.sql2o.quirks.PostgresQuirks;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;
import ru.yandex.qatools.embed.postgresql.distribution.Version;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

@Configuration
public class DataSourceConfig {

  private static final List<String> DEFAULT_ADDITIONAL_INIT_DB_PARAMS = Arrays.asList("--nosync", "--locale=en_US.UTF-8");

  @Bean
  @DependsOn("postgresProcess")
  public DataSource dataSource(PostgresConfig config) {
    DriverManagerDataSource ds = new DriverManagerDataSource();
    ds.setDriverClassName("org.postgresql.Driver");
    ds.setUrl(format("jdbc:postgresql://%s:%s/%s", config.net().host(), config.net().port(), config.storage().dbName()));
    ds.setUsername(config.credentials().username());
    ds.setPassword(config.credentials().password());
    return ds;
  }


  @Bean
  public PostgresConfig postgresConfig() throws IOException {
    final PostgresConfig postgresConfig = new PostgresConfig(Version.V11_1,
    new AbstractPostgresConfig.Net("localhost", Network.getFreeServerPort()),
    new AbstractPostgresConfig.Storage("test"),
    new AbstractPostgresConfig.Timeout(),
    new AbstractPostgresConfig.Credentials("user", "pass")
    );
    postgresConfig.getAdditionalInitDbParams().addAll(DEFAULT_ADDITIONAL_INIT_DB_PARAMS);
    return postgresConfig;
  }

  @Bean(destroyMethod = "stop")
  public PostgresProcess postgresProcess(PostgresConfig config) throws IOException {
    PostgresStarter<PostgresExecutable, PostgresProcess> runtime = PostgresStarter.getDefaultInstance();
    PostgresExecutable exec = runtime.prepare(config);
    PostgresProcess process = exec.start();
    return process;
  }

  @Bean()
  @DependsOn("postgresProcess")
  public Sql2o sql2o(final DataSource dataSource) {
    return new Sql2o(dataSource, new PostgresQuirks());
  }
}

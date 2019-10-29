/*
 * Copyright 2019 Yaakov Chaikin (yaakov@ClearlyDecoded.com). Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the License is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */
package org.mongounit.config;

import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

/**
 * {@link MongoDbFactoryBean} class is a definition of the MongoUnit specific {@link MongoDbFactory}
 * bean to be substituted for originally configured one in the Spring context.
 */
public class MongoDbFactoryBean
    implements FactoryBean<MongoDbFactory>, EnvironmentAware, InitializingBean {

  /**
   * Spring environment.
   */
  private Environment environment;

  /**
   * Mongo client URI to use for the new MongoDbFactory bean.
   */
  private MongoClientURI mongoClientURI;

  @Override
  public MongoDbFactory getObject() {

    // Create new factory based on the calculated URI
    return new SimpleMongoClientDbFactory(mongoClientURI.getURI());
  }

  @Override
  public Class<?> getObjectType() {
    return MongoDbFactory.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  @Override
  public void afterPropertiesSet() {
    // Set mongo client URI based on the mongounit properties and spring data mongo properties
    mongoClientURI = MongoUnitConfigurationUtil.generateNewMongoClientURI(environment);
  }

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }
}

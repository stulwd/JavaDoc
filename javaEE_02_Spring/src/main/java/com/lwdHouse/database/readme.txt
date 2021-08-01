Spring为了简化数据库访问，主要做了以下几点工作:
    1. 提供了简化的访问JDBC的模板类，不必手动释放资源；
    2. 提供了一个统一的DAO类以实现Data Access Object模式；
    3. 把SQLException封装为DataAccessException，这个异常是一个RuntimeException，
        并且让我们能区分SQL异常的原因，例如，DuplicateKeyException表示违反了一个唯一约束；
    4. 能方便地集成Hibernate、JPA和MyBatis这些数据库访问框架。
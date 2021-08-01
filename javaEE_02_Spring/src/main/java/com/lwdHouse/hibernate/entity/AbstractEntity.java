package com.lwdHouse.hibernate.entity;

import javax.persistence.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

// 对于AbstractEntity来说，我们要标注一个@MappedSuperclass表示它用于继承
@MappedSuperclass
public class AbstractEntity {

    // 主键id定义的类型不是long，而是Long。
    // 这是因为Hibernate如果检测到主键为null，就不会在INSERT语句中指定主键的值，
    // 而是返回由数据库生成的自增值，否则，Hibernate认为我们的程序指定了主键的值，
    // 会在INSERT语句中直接列出。long型字段总是具有默认值0，因此，每次插入的主键值总是0，导致除第一次外后续插入都将失败
    private Long id;
    // 没有使用long，而是Long，这是因为使用基本类型会导致某种查询会添加意外的条件，
    // 后面我们会详细讨论，这里只需牢记，作为映射使用的JavaBean，所有属性都使用包装类型而不是基本类型
    // ！！！使用Hibernate时，不要使用基本类型的属性，总是使用包装类型，如Long或Integer。
    private Long createdAt;


    // 对于主键，还需要用@Id标识
    @Id
    // 自增主键再追加一个@GeneratedValue, 以便Hibernate能读取到自增主键的值
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // nullable指示列是否允许为NULL
    // updatable指示该列是否允许被用在UPDATE语句, 默认是true
    // length指示String类型的列的长度, 默认是255
    @Column(nullable = false, updatable = false)
    public Long getId() {
        System.out.println("获取id");
        return id;
    }

    // 一定要有setter方法，因为，Hibernate框架读取到数据库的数据，是通过调用set方法来赋值的
    public void setId(Long id) {
        System.out.println("设置id");
        this.id = id;
    }

    @Column(nullable = false, updatable = false)
    public Long getCreatedAt() {
        System.out.println("获取createdAt");
        return createdAt;
    }

    // 返回一个“虚拟”的属性
    // 因为getCreatedDateTime()是计算得出的属性
    // 因此必须要标注@Transient，否则Hibernate会尝试从数据库读取名为createdDateTime这个不存在的字段从而出错
    @Transient
    public ZonedDateTime getCreateDateTime(){
        return Instant.ofEpochMilli(this.getCreatedAt()).atZone(ZoneId.systemDefault());
    }

    // 在我们将一个JavaBean持久化到数据库之前（即执行INSERT语句），
    // Hibernate会先执行该方法，这样我们就可以自动设置好createdAt属性.
    // 在持久化的时候，会调用所有带@Column的方法，保存到数据库
    @PrePersist
    public void preInsert(){
        setCreatedAt(System.currentTimeMillis());
    }

    public void setCreatedAt(Long createdAt) {
        System.out.println("设置createdAt");
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AbstractEntity{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                '}';
    }
}

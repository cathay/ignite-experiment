/*
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package com.mstage.ignite;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Indexed;
import org.bson.types.ObjectId;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Person class.
 */
@Entity("employees")
public class Employee implements Serializable {
    /** ID generator. */
    private static final AtomicLong IDGEN = new AtomicLong(System.currentTimeMillis());

    /** Required by Morphia. */
    @Id
    private ObjectId objectId;

    /** First name. */
    private String name;

    /** Salary. */
    private double salary;

    /**
     * Required by Morphia.
     */
    private Employee() {
        // No-op.
    }

    public Employee(ObjectId objectId, String name, double salary) {
        this.name = name;
        this.salary = salary;
        this.objectId  = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return "Employee [firstName=" + name +

                ", objectid=" + objectId +
            ", salary=" + salary + ']';
    }
}


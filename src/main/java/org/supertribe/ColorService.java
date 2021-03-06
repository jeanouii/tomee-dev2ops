/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.supertribe;

import org.apache.openejb.api.Monitor;
import org.tomitribe.sabot.Config;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Lock;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import static javax.ejb.LockType.READ;
import static javax.ejb.LockType.WRITE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Lock(READ)
@Singleton
@Path("/color")
@Monitor
public class ColorService {

    @Inject
    @Config("color.name")
    private String name;

    @Inject
    @Config("color.red")
    private Integer r;

    @Inject
    @Config("color.green")
    private Integer g;

    @Inject
    @Config("color.blue")
    private Integer b;

    private String activeColor;

    public ColorService() {
        this.activeColor = "white";
    }

    @GET
    @Monitor
    public String getColor() {
        return activeColor;
    }

    @Path("{color}")
    @POST
    @Lock(WRITE)
    @Monitor
    public void setColor(@PathParam("color") String color) {
        this.activeColor = color;
    }

    @Path("object")
    @GET
    @Produces({APPLICATION_JSON})
    @RolesAllowed({"mans-best-friend"})
    @Monitor
    public Color getColorObject() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new Color(name, r, g, b);
    }
}

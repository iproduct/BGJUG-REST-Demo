/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2003-2014 IPT - Intellectual Products & Technologies.
 * All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 with Classpath Exception only ("GPL"). 
 * You may use this file only in compliance with GPL. You can find a copy 
 * of GPL in the root directory of this project in the file named LICENSE.txt.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the GPL file named LICENSE.txt in the root directory of 
 * the project.
 *
 * GPL Classpath Exception:
 * IPT - Intellectual Products & Technologies (IPT) designates this particular 
 * file as subject to the "Classpath" exception as provided by IPT in the GPL 
 * Version 2 License file that accompanies this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 */
package org.iproduct.polling;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.iproduct.polling.controller.AlternativeController;
import org.iproduct.polling.controller.PollController;
import org.iproduct.polling.controller.VoteController;

/**
 *
 *
 * @author Trayan Iliev, IPT [http://iproduct.org]
 */
public class CustomBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(PollController.class).to(PollController.class);
        bind(AlternativeController.class).to(AlternativeController.class);
        bind(VoteController.class).to(VoteController.class);
    }
}

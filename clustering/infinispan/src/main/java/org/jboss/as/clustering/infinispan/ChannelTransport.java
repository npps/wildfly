/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.clustering.infinispan;

import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.factories.annotations.Inject;
import org.infinispan.remoting.transport.jgroups.JGroupsTransport;
import org.jgroups.Channel;

/**
 * Custom {@link JGroupsTransport} that uses a provided channel.
 * @author Paul Ferraro
 */
public class ChannelTransport extends JGroupsTransport {

    public ChannelTransport(Channel channel) {
        super(channel);
    }

    @Override
    @Inject
    public void setConfiguration(GlobalConfiguration config) {
        // Turn off global jmx statistics so that Infinispan doesn't attempt to register/unregister channel/protocol mbeans.
        this.configuration = new GlobalConfigurationBuilder().read(config).globalJmxStatistics().disable().build();
    }

    @Override
    protected synchronized void initChannel() {
        this.channel.setDiscardOwnMessages(false);
        this.connectChannel = true;
        this.disconnectChannel = true;
        this.closeChannel = false;
    }
}

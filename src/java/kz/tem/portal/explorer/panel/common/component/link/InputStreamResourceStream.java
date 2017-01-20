package kz.tem.portal.explorer.panel.common.component.link;


import java.io.IOException;
import java.io.InputStream;

import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

/**
 * Simple implementation of {@link IResourceStream} that provides a given
 * {@link InputStream} to be handled by a {@link ResourceStreamRequestHandler}.
 *
 * This enables {@link InputStreamDownloadLink} to serve content coming from
 * {@link InputStream}s without having to copy the stream content into temp
 * files.
 * 
 * @see InputStreamDownloadLink
 *
 * @author Martin Spielmann
 */
public class InputStreamResourceStream extends AbstractResourceStream {

	private static final long serialVersionUID = 4184386569788933731L;

	private InputStream stream;

	/**
	 * Instantiates a new input stream resource stream.
	 *
	 * @param stream
	 *            the stream
	 */
	public InputStreamResourceStream(InputStream stream) {
		this.stream = stream;
	}

	@Override
	public InputStream getInputStream() throws ResourceStreamNotFoundException {
		return stream;
	}

	@Override
	public void close() throws IOException {

		if (stream != null) {
			stream.close();
			stream = null;
		}
	}
}
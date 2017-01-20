package kz.tem.portal.explorer.panel.common.component.link;

import java.io.InputStream;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;

/**
 * The InputStreamDownloadLink provides a simple version of a download link that
 * serves content from a given {@link InputStream}.
 *
 * @author Martin Spielmann
 */
public class InputStreamDownloadLink extends Link<InputStream> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new input stream download link.
	 *
	 * @param id
	 *            the id
	 * @param stream
	 *            the stream to be downloaded
	 * @param displayName
	 *            the display name / file name
	 */
	public InputStreamDownloadLink(String id, IModel<InputStream> stream,
			IModel<String> displayName) {
		super(id, stream);
		setBody(displayName);
	}

	@Override
	public void onClick() {
		final InputStream stream = getModelObject();
		if (stream == null) {
			throw new IllegalStateException(getClass().getName()
					+ " failed to retrieve a File object from model");
		}

		InputStreamResourceStream resourceStream = new InputStreamResourceStream(
				stream);
		// maybe file name and display name should be seperated?
		ResourceStreamRequestHandler requestHandler = new ResourceStreamRequestHandler(
				resourceStream, String.valueOf(getBody().getObject()));
		requestHandler.setContentDisposition(ContentDisposition.ATTACHMENT);
		getRequestCycle().scheduleRequestHandlerAfterCurrent(requestHandler);
	}

	@Override
	public void detachModels() {
		super.detachModels();
	}
}
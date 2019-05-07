package io.server.omen.endpoints;

import io.server.util.MD5;
import io.server.util.Utility;
import neytorokx.model.IEndpoint;
import neytorokx.model.Request;
import neytorokx.model.Response;

public class RegisterComputerEndpoint implements IEndpoint {

	@Override
	public void process(Request req, Response res) {
		
		req.getQuery().forEach((k, v) -> {
			if (k.toLowerCase().contentEquals("cname")) {
				String response = MD5.hash(Utility.salt(v));
				res.sendHeaders(200, response.length());
				res.send(response);
			}
		});
		res.close();

	}

}

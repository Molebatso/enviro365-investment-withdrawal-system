/**
 * REST controllers: the HTTP entry points into the application.
 *
 * Controllers are intentionally "thin" - they accept requests, delegate
 * to the service layer, and shape the HTTP response. They must not
 * contain business logic (balance calculations, rule validation, etc.);
 * that belongs in the service layer so it can be unit tested without
 * spinning up a web server.
 */
package com.enviro.assessment.junior.candidate.controller;

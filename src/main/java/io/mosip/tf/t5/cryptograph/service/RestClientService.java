package io.mosip.tf.t5.cryptograph.service;

import java.util.List;

import org.springframework.http.MediaType;

import io.mosip.tf.t5.cryptograph.constant.ServiceConstant;
import io.mosip.tf.t5.cryptograph.exception.ApisResourceAccessException;

/**
 * The Interface RegistrationProcessorRestClientService.
 *
 * 
 * @param <T>
 *            the generic type
 */
public interface RestClientService<T> {

	/**
	 * Gets the api.
	 *
	 * @param apiName
	 *            the api name
	 * @param pathsegments
	 *            pathsegments of the uri
	 * @param queryParam
	 *            the query param
	 * @param queryParamValue
	 *            the query param value
	 * @param responseType
	 *            the response type
	 * @return the api
	 * @throws ApisResourceAccessException
	 *             the apis resource access exception
	 */
	public T getApi(ServiceConstant apiName, List<String> pathsegments, String queryParam, String queryParamValue,
			Class<?> responseType) throws ApisResourceAccessException;

	public T getApi(ServiceConstant apiName, List<String> pathsegments, List<String> queryParam, List<Object> queryParamValue,
					Class<?> responseType) throws ApisResourceAccessException;

	/**
	 * Post api.
	 *
	 * @param apiName
	 *            the api name
	 * @param queryParam
	 *            the query param
	 * @param queryParamValue
	 *            the query param value
	 * @param requestedData
	 *            the requested data
	 * @param responseType
	 *            the response type
	 * @return the t
	 * @throws ApisResourceAccessException
	 *             the apis resource access exception
	 */
	public T postApi(ServiceConstant apiName, String queryParam, String queryParamValue, T requestedData, Class<?> responseType)
			throws ApisResourceAccessException;

	/**
	 * Post api.
	 *
	 * @param apiName
	 *            the api name
	 * @param queryParamName
	 *            the query param
	 * @param queryParamValue
	 *            the query param value
	 * @param requestedData
	 *            the requested data
	 * @param responseType
	 *            the response type
	 * @param mediaType
	 * 			  the content type    
	 *        
	 * @return the t
	 * @throws ApisResourceAccessException
	 *             the apis resource access exception
	 */
	public T postApi(ServiceConstant apiName, String queryParamName, String queryParamValue, T requestedData,
			Class<?> responseType, MediaType mediaType) throws ApisResourceAccessException ;
	/**
	 * Post api.
	 *
	 * @param apiName
	 *            the api name
	 * @param pathsegments
	 *            the pathsegments
	 * @param queryParam
	 *            the query param
	 * @param queryParamValue
	 *            the query param value
	 * @param requestedData
	 *            the requested data
	 * @param responseType
	 *            the response type
	 * @return the t
	 * @throws ApisResourceAccessException
	 *             the apis resource access exception
	 */

	public T postApi(ServiceConstant apiName, List<String> pathsegments, String queryParam, String queryParamValue,
			T requestedData, Class<?> responseType) throws ApisResourceAccessException;

	/**
	 * Post Api
	 *
	 * @param apiName
	 * @param mediaType
	 * @param pathsegments
	 * @param queryParam
	 * @param queryParamValue
	 * @param requestedData
	 * @param responseType
	 * @return
	 * @throws ApisResourceAccessException
	 */
	public T postApi(ServiceConstant apiName, MediaType mediaType, List<String> pathsegments, List<String> queryParam, List<Object> queryParamValue,
					 T requestedData, Class<?> responseType) throws ApisResourceAccessException;

	/**
	 * Patch api.
	 *
	 * @param apiName
	 *            the api name
	 * @param pathsegments
	 *            the pathsegments
	 * @param queryParam
	 *            the query param
	 * @param queryParamValue
	 *            the query param value
	 * @param requestedData
	 *            the requested data
	 * @param responseType
	 *            the response type
	 * @return the t
	 * @throws ApisResourceAccessException
	 *             the apis resource access exception
	 */
	public T patchApi(ServiceConstant apiName, List<String> pathsegments, String queryParam, String queryParamValue,
			T requestedData, Class<?> responseType) throws ApisResourceAccessException;

	/**
	 * Put api.
	 *
	 * @param apiName the api name
	 * @param pathsegments the pathsegments
	 * @param queryParam the query param
	 * @param queryParamValue the query param value
	 * @param requestedData the requested data
	 * @param responseType the response type
	 * @return the t
	 * @throws ApisResourceAccessException 
	 */
	public T putApi(ServiceConstant apiName, List<String> pathsegments, String queryParam, String queryParamValue,
			T requestedData, Class<?> responseType,MediaType mediaType) throws ApisResourceAccessException;
}

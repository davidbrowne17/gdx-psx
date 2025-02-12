/*******************************************************************************
 * Copyright 2022 Matvey Zholudz
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package by.fxg.gdxpsx.g3d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.BaseShaderProvider;

/** PSXShader provider for the {@link ModelBatch} and replacement for previously used {@link ShaderTransformer}.
 *  PSXShader is shader made out of default libgdx 3d shader with some another features such as: <ul>
 *  <li>Per-renderable PSX Vertex jitter effect</li>
 *  <li>Per-renderable PSX Texture affineness(aka broken perspective)</li>
 *  <li>Per-renderable Lookup table texture support</li>
 *  <li>SpotLights support</li></ul>
 *  @author fxgaming (FXG) 
 */
public class PSXShaderProvider extends BaseShaderProvider {
	public static final PSXShaderType DEFAULT_SHADER_TYPE = PSXShaderType.LIT_PerFragmentLighting;
	public final DefaultShader.Config config;

	public PSXShaderProvider() {
		this(DEFAULT_SHADER_TYPE, new DefaultShader.Config());
	}
	
	public PSXShaderProvider(final FileHandle vertexShader, final FileHandle fragmentShader) {
		this(vertexShader.readString(), fragmentShader.readString());
	}
	
	public PSXShaderProvider(final String vertexShader, final String fragmentShader) {
		this(null, new DefaultShader.Config(vertexShader, fragmentShader));
	}
	
	public PSXShaderProvider(final DefaultShader.Config config) {
		this(null, config);
	}
	
	/** @param shaderType - Type of {@link PSXShader}
	 *  @param config - Shader config **/
	public PSXShaderProvider(PSXShaderType shaderType, final DefaultShader.Config config) {
		this.config = config == null ? new DefaultShader.Config() : config;
		if ((this.config.vertexShader == null || this.config.fragmentShader == null) && shaderType == null) shaderType = PSXShaderType.LIT_PerFragmentLighting;
		if (shaderType != null) {
			switch (shaderType) {
				case LIT_PerVertexLighting: {
					this.config.vertexShader = Gdx.files.classpath("by/fxg/gdxpsx/shaders/litvertex.vert").readString();
					this.config.fragmentShader = Gdx.files.classpath("by/fxg/gdxpsx/shaders/litvertex.frag").readString();
				} break;
				case LIT_PerFragmentLighting: {
					this.config.vertexShader = Gdx.files.classpath("by/fxg/gdxpsx/shaders/litpixel.vert").readString();
					this.config.fragmentShader = Gdx.files.classpath("by/fxg/gdxpsx/shaders/litpixel.frag").readString();
				} break;
			}
		}
	}

	protected Shader createShader(final Renderable renderable) {
		return new PSXShader(renderable, this.config);
	}
}

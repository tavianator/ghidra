/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ghidra.app.util.bin.format.dwarf4.external;

import ghidra.app.util.bin.format.elf.GnuBuildIdSection;
import ghidra.app.util.bin.format.elf.GnuBuildIdSection.GnuBuildIdValues;
import ghidra.app.util.bin.format.elf.GnuDebugLinkSection;
import ghidra.app.util.bin.format.elf.GnuDebugLinkSection.GnuDebugLinkSectionValues;
import ghidra.program.model.listing.Program;
import ghidra.util.NumericUtilities;

/**
 * Information needed to find an ELF/DWARF external debug file, found in an ELF binary's
 * ".gnu_debuglink" section and/or ".note.gnu.build-id" section.  
 * <p>
 * The debuglink can provide a filename and crc of the external debug file, while the build-id
 * can provide a hash that is converted to a filename that identifies the external debug file.
 */
public class ExternalDebugInfo {

	public static ExternalDebugInfo fromProgram(Program program) {
		GnuDebugLinkSectionValues debugLinkValues = GnuDebugLinkSection.fromProgram(program);
		GnuBuildIdValues buildIdValues = GnuBuildIdSection.fromProgram(program);
		if (buildIdValues != null && !buildIdValues.isValid()) {
			buildIdValues = null;
		}
		if (debugLinkValues == null || buildIdValues == null) {
			return null;
		}

		return new ExternalDebugInfo(debugLinkValues.getFilename(), debugLinkValues.getCrc(),
			buildIdValues != null ? buildIdValues.getDescription() : null);
	}

	private String filename;
	private int crc;
	private byte[] hash;

	public ExternalDebugInfo(String filename, int crc, byte[] hash) {
		this.filename = filename;
		this.crc = crc;
		this.hash = hash;
	}

	public String getFilename() {
		return filename;
	}

	public int getCrc() {
		return crc;
	}

	public byte[] getHash() {
		return hash;
	}

	@Override
	public String toString() {
		return String.format("ExternalDebugInfo [filename=%s, crc=%s, hash=%s]",
			filename,
			Integer.toHexString(crc),
			NumericUtilities.convertBytesToString(hash));
	}
}

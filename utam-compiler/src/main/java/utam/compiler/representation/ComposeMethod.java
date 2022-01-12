/*
 * Copyright (c) 2021, salesforce.com, inc.
 * All rights reserved.
 * SPDX-License-Identifier: MIT
 * For full license text, see the LICENSE file in the repo root
 * or https://opensource.org/licenses/MIT
 */
package utam.compiler.representation;

import static utam.compiler.helpers.TypeUtilities.VOID;

import java.util.ArrayList;
import java.util.List;
import utam.compiler.helpers.ParameterUtils;
import utam.core.declarative.representation.MethodParameter;
import utam.core.declarative.representation.PageObjectMethod;
import utam.core.declarative.representation.TypeProvider;

/**
 * business method is a sequence of internal method calls
 *
 * @author elizaveta.ivanova
 * @since 226
 */
public class ComposeMethod implements PageObjectMethod {

  private final String name;
  private final List<MethodParameter> parameters;
  private final List<String> code = new ArrayList<>();
  private final List<TypeProvider> classImports = new ArrayList<>();
  private final List<TypeProvider> imports = new ArrayList<>();
  private final TypeProvider returns;

  /**
   * Initializes a new instance of the ComposeMethod class
   * @param methodName the name of the method
   * @param returnType the return type of the method
   * @param parameters the list of parameters of the method
   * @param statements the list of statments of the method
   */
  public ComposeMethod(String methodName,
      TypeProvider returnType,
      List<MethodParameter> parameters,
      List<ComposeMethodStatement> statements) {
    this.name = methodName;
    this.returns = returnType;
    statements.forEach(
        statement -> {
          code.addAll(statement.getCodeLines());
          ParameterUtils.setImports(imports, statement.getImports());
          ParameterUtils.setImports(classImports, statement.getClassImports());
        });
    this.parameters = new ArrayList<>(parameters);
    if(!returnType.isSameType(VOID)) {
      ParameterUtils.setImport(imports, returnType);
      ParameterUtils.setImport(classImports, returnType);
    }
  }

  @Override
  public MethodDeclarationImpl getDeclaration() {
    return new MethodDeclarationImpl(name, parameters, returns, imports);
  }

  @Override
  public List<TypeProvider> getClassImports() {
    return classImports;
  }

  @Override
  public List<String> getCodeLines() {
    return code;
  }

  @Override
  public boolean isPublic() {
    return true;
  }

}
